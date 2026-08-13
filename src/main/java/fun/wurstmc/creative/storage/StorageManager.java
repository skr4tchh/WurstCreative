package fun.wurstmc.creative.storage;

import com.google.gson.Gson;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.rocksdb.*;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class StorageManager {

    private Plugin plugin;

    private final Gson GSON = new Gson();

    private final ExecutorService readExecutor = Executors.newVirtualThreadPerTaskExecutor();

    private File dbFolder;

    private DBOptions dbOptions;

    private RocksDB database;

    private final Map<StorageType, ColumnFamilyHandle> handles = new EnumMap<>(StorageType.class);

    private final List<ColumnFamilyHandle> nativeHandlesList = new ArrayList<>();

    public StorageManager(Plugin plugin) {
        this.plugin = plugin;
        this.dbFolder = new File(plugin.getDataFolder(), "database");
    }

    public void init() {
        RocksDB.loadLibrary();

        if (!dbFolder.exists()) {
            dbFolder.mkdirs();
        }

        this.dbOptions = new DBOptions()
                .setCreateIfMissing(true)
                .setCreateMissingColumnFamilies(true);

        List<ColumnFamilyDescriptor> descriptors = new ArrayList<>();
        descriptors.add(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY));

        for (StorageType type : StorageType.values()) {
            descriptors.add(new ColumnFamilyDescriptor(type.getName().getBytes(StandardCharsets.UTF_8)));
        }

        try {
            this.database = RocksDB.open(dbOptions, dbFolder.getAbsolutePath(), descriptors, nativeHandlesList);

            StorageType[] types = StorageType.values();
            for (int i = 0; i < types.length; i++) {
                handles.put(types[i], nativeHandlesList.get(i + 1));
            }
        } catch (RocksDBException ex) {
            plugin.getLogger().log(Level.SEVERE, "Ошибка при инициализации RocksDB!", ex);
        }
    }

    public void shutdown() {
        try {
            for (ColumnFamilyHandle handle : nativeHandlesList) {
                if (handle != null) handle.close();
            }
            if (database != null) database.close();
            if (dbOptions != null) dbOptions.close();
            readExecutor.shutdown();
        } finally {
            this.nativeHandlesList.clear();
            this.handles.clear();
            this.database = null;
            this.dbOptions = null;
        }
    }

    public <T> @NotNull Int2ObjectMap<T> load(@NotNull StorageType storage) {
        ColumnFamilyHandle handle = handles.get(storage);
        Int2ObjectMap<T> results = new Int2ObjectOpenHashMap<>();
        if (handle == null) return results;

        Class<T> classType = (Class<T>) storage.getClassType();

        try (RocksIterator iterator = database.newIterator(handle)) {
            for (iterator.seekToFirst(); iterator.isValid(); iterator.next()) {
                int worldId = ByteBuffer.wrap(iterator.key()).getInt();
                String json = new String(iterator.value(), StandardCharsets.UTF_8);
                T object = GSON.fromJson(json, classType);

                results.put(worldId, object);
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Ошибка при сканировании хранилища " + storage.getName(), e);
        }

        return results;
    }

    public <T> void save(@NotNull StorageType storage, @NotNull Int2ObjectMap<T> mapToSave) {
        if (mapToSave.isEmpty()) return;

        ColumnFamilyHandle handle = handles.get(storage);
        if (handle == null) return;

        try (WriteBatch batch = new WriteBatch(); WriteOptions writeOptions = new WriteOptions()) {

            for (Map.Entry<Integer, T> entry : mapToSave.entrySet()) {
                byte[] key = ByteBuffer.allocate(Integer.BYTES).putInt(entry.getKey()).array();
                byte[] value = GSON.toJson(entry.getValue()).getBytes(StandardCharsets.UTF_8);

                batch.put(handle, key, value);
            }

            database.write(writeOptions, batch);

        } catch (RocksDBException e) {
            plugin.getLogger().log(Level.SEVERE, "Критическая ошибка пакетного сохранения в " + storage.getName(), e);
        }
    }

    public void delete(@NotNull StorageType storage, int worldId) {
        ColumnFamilyHandle handle = handles.get(storage);
        if (handle == null) return;

        byte[] key = ByteBuffer.allocate(Integer.BYTES).putInt(worldId).array();
        try {
            database.delete(handle, key);
        } catch (RocksDBException e) {
            e.printStackTrace();
        }
    }

}
