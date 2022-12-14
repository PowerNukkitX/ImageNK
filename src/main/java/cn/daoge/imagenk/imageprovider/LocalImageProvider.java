package cn.daoge.imagenk.imageprovider;

import cn.daoge.imagenk.ImageNK;
import cn.nukkit.Player;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * 本地图片存储实现类
 */
@Getter
public class LocalImageProvider extends CachedImageProvider {
    protected Path rootPath;

    public LocalImageProvider(Path rootPath) {
        this.rootPath = rootPath;
        //检查文件夹可用性
        if (!Files.exists(rootPath)) {
            try {
                Files.createDirectories(rootPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Map<String, BufferedImage> loadAll(Player notifier) {
        var all = new HashMap<String, BufferedImage>();
        try (var stream = Files.walk(rootPath, 1)) {
            stream.filter(Files::isRegularFile).forEach(path -> {
                var name = path.getName(path.getNameCount() - 1).toString();
                BufferedImage image;
                try {
                    image = ImageIO.read(path.toFile());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                var logger = ImageNK.getInstance().getLogger();
                String message;
                if (image == null) {
                    logger.warning(message = "§cUnable to load image: §f" + name);
                    if (notifier != null) notifier.sendMessage("[ImageNK] " + message);
                    return;
                }
                logger.info(message = "§aSuccessfully load image: §f" + name);
                if (notifier != null) notifier.sendMessage("[ImageNK] " + message);
                all.put(name, image);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return all;
    }
}
