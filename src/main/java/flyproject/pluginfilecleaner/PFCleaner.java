package flyproject.pluginfilecleaner;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.swing.*;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PFCleaner {
    public static void main(String[] args) throws Exception {
        int n = JOptionPane.showConfirmDialog(null, "请遵守使用协议(EULA)", "警告",JOptionPane.YES_NO_OPTION);
        if (!(n==JOptionPane.OK_OPTION)){
            System.out.println("由于未同意使用协议(EULA)协议 软件已关闭");
            exit();
        }
        System.out.println("[PFCleaner]  请输入服务端目录(不要输入plugins文件夹的目录)");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = br.readLine();
        System.out.println("[PFCleaner] 服务端目录: " + str);
        File path = new File(str + "/plugins");
        if (!path.exists()){
            System.err.println("[PFCleaner] 目录不存在");
            exit();
        }
        List<String> plugins = new ArrayList<>();
        for (File f : path.listFiles()){
            if (f.getName().endsWith(".jar")){
                JarFile jar = new JarFile(f);
                JarEntry entry = jar.getJarEntry("plugin.yml");
                InputStream is = jar.getInputStream(entry);
                FileConfiguration p = YamlConfiguration.loadConfiguration(tofile(is));
                String name = p.get("name").toString();
                plugins.add(name);
            }
        }
        if (plugins.size()==0){
            System.err.println("[PFCleaner] 并没有可用的插件");
            exit();
        }
        System.out.println("[PFCleaner] 初始化插件完成 可用插件数量: " + plugins.size());
        for (File pf : path.listFiles()){
            if (pf.isDirectory()){
                if (!plugins.contains(pf.getName())){
                    
                }
            }
        }
    }
    private static File tofile(InputStream inputStream) throws IOException {
        File file = File.createTempFile("pfc-temp",".yml");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {

            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            // commons-io
            //IOUtils.copy(inputStream, outputStream);

        }
        return file;
    }
    private static void exit(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
