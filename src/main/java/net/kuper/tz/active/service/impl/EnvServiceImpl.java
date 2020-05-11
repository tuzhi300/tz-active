package net.kuper.tz.active.service.impl;

import net.kuper.tz.active.service.EnvService;
import net.kuper.tz.core.controller.exception.ApiException;
import net.kuper.tz.core.properties.BuildProperties;
import net.kuper.tz.core.utils.DateUtils;
import org.hyperic.sigar.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.*;

@Service
public class EnvServiceImpl implements EnvService {

    @Autowired
    private BuildProperties properties;

    @Override
    public Map<String, Object> application() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long startTime = runtimeMXBean.getStartTime();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("app.version", properties.getVersion());
        result.put("app.build.time", properties.getTime());
        result.put("app.launch.class", runtimeMXBean.getBootClassPath());
        result.put("app.launch.time", DateUtils.date2String(new Date(startTime)));
        result.put("app.run.time", runtimeMXBean.getUptime());
        result.put("app.thread.count", ManagementFactory.getThreadMXBean().getThreadCount());
        result.put("app.thread.peak.count", ManagementFactory.getThreadMXBean().getPeakThreadCount());
        result.put("app.thread.daemon.count", ManagementFactory.getThreadMXBean().getDaemonThreadCount());
        result.put("app.thread.started.count", ManagementFactory.getThreadMXBean().getTotalStartedThreadCount());
        result.put("app.process.id", getProcessID());
        Properties props = System.getProperties();
        result.put("java.version", props.getProperty("java.version"));
        result.put("java.vendor", props.getProperty("java.vendor"));
        result.put("java.vendor.url", props.getProperty("java.vendor.url"));
        result.put("java.home", props.getProperty("java.home"));
        result.put("java.vm.specification.version", props.getProperty("java.vm.specification.version"));
        result.put("java.vm.specification.vendor", props.getProperty("java.vm.specification.vendor"));
        result.put("java.vm.specification.name", props.getProperty("java.vm.specification.name"));
        result.put("java.vm.version", props.getProperty("java.vm.version"));
        result.put("java.vm.vendor", props.getProperty("java.vm.vendor"));
        result.put("java.vm.name", props.getProperty("java.vm.name"));
        result.put("java.specification.version", props.getProperty("java.specification.version"));
        result.put("java.specification.vendor", props.getProperty("java.specification.vendor"));
        result.put("java.specification.name", props.getProperty("java.specification.name"));
        result.put("java.class.version", props.getProperty("java.class.version"));
        result.put("java.class.path", props.getProperty("java.class.path"));
        result.put("java.library.path", props.getProperty("java.library.path"));
        result.put("java.io.tmpdir", props.getProperty("java.io.tmpdir"));
        result.put("java.ext.dirs", props.getProperty("java.ext.dirs"));
        result.put("os.name", props.getProperty("os.name"));
        result.put("os.arch", props.getProperty("os.arch"));
        result.put("os.version", props.getProperty("os.version"));
        result.put("file.separator", props.getProperty("file.separator"));
        result.put("path.separator", props.getProperty("path.separator"));
        result.put("line.separator", props.getProperty("line.separator"));
        result.put("user.name", props.getProperty("user.name"));
        result.put("user.home", props.getProperty("user.home"));
        result.put("user.dir", props.getProperty("user.dir"));
        return result;
    }

    private final int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        System.out.println(runtimeMXBean.getName());
        return Integer.valueOf(runtimeMXBean.getName().split("@")[0])
                .intValue();
    }


    @Override
    public Map<String, Object> vmMemory() {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            Runtime r = Runtime.getRuntime();
            result.put("memory.total", r.totalMemory() / 1024L);
            result.put("memory.free", r.freeMemory() / 1024L);
            result.put("memory.max", r.maxMemory() / 1024L);
            result.put("available.processors", r.availableProcessors());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return result;
    }

    @Override
    public Map<String, Object> memory() {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            Sigar sigar = new Sigar();
            Mem mem = sigar.getMem();
            // 内存总量
            result.put("mem.total", mem.getTotal() / 1024L);
            // 当前内存使用量
            result.put("mem.used", mem.getUsed() / 1024L);
            // 当前内存剩余量
            result.put("mem.free", mem.getFree() / 1024L);
            Swap swap = sigar.getSwap();
            // 交换区总量
            result.put("sw.total", swap.getTotal() / 1024L);
            // 当前交换区使用量
            result.put("sw.used", swap.getUsed() / 1024L);
            // 当前交换区剩余量
            result.put("sw.free", swap.getFree() / 1024L);
        } catch (SigarException e) {
            throw new ApiException(e);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> cpu() {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            Sigar sigar = new Sigar();
            CpuInfo infos[] = sigar.getCpuInfoList();
            CpuPerc cpuList[] = sigar.getCpuPercList();
            for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
                Map<String, Object> result = new LinkedHashMap<>();
                CpuInfo info = infos[i];
                result.put("cup", (i + 1));
                result.put("cpu.mhz", info.getMhz());// CPU的总量MHz
                result.put("cup.vendor", info.getVendor());// 获得CPU的卖主，如Intel
                result.put("cpu.model", info.getModel());// 获得CPU的类别，如Celeron
                result.put("cpu.cache.size", info.getCacheSize());// 缓冲存储器数量
                if (cpuList[i] != null) {
                    result.put("cpu.percent.user", cpuList[i].getUser());// 用户使用率
                    result.put("cpu.percent.sys", cpuList[i].getSys());// 系统使用率
                    result.put("cpu.percent.wait", cpuList[i].getWait());// 当前等待率
                    result.put("cpu.percent.nice", cpuList[i].getNice());//
                    result.put("cpu.percent.idle", cpuList[i].getIdle());// 当前空闲率
                    result.put("cpu.percent.combined", cpuList[i].getCombined());// 总的使用率
                }
                list.add(result);
            }
        } catch (SigarException e) {
            throw new ApiException(e);
        }
        return list;
    }

    @Override
    public Map<String, Object> os() {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            OperatingSystem OS = OperatingSystem.getInstance();
            // 操作系统内核类型如： 386、486、586等x86
            result.put("os.arch", OS.getArch());
            result.put("os.cpu.endian", OS.getCpuEndian());//
            result.put("os.data.model", OS.getDataModel());//
            // 系统描述
            result.put("os.description", OS.getDescription());
            // 操作系统类型
            result.put("os.name", OS.getName());
            result.put("os.patch.level", OS.getPatchLevel());//
            // 操作系统的卖主
            result.put("os.vendor", OS.getVendor());
            // 卖主名称
            result.put("os.vendor.code.name", OS.getVendorCodeName());
            // 操作系统名称
            result.put("os.vendor.name", OS.getVendorName());
            // 操作系统卖主类型
            result.put("os.vendor.version", OS.getVendorVersion());
            // 操作系统的版本号
            result.put("os.version", OS.getVersion());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> disk() {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            Sigar sigar = new Sigar();
            FileSystem fslist[] = sigar.getFileSystemList();
            for (int i = 0; i < fslist.length; i++) {
                Map<String, Object> result = new LinkedHashMap<>();
                result.put("disk", i);
                FileSystem fs = fslist[i];
                // 分区的盘符名称
                result.put("disk.dev.name", fs.getDevName());
                // 分区的盘符名称
                result.put("disk.dir.name", fs.getDirName());
                result.put("disk.flags", fs.getFlags());//
                // 文件系统类型
                result.put("disk.type", fs.getType());
                // 文件系统类型名，比如本地硬盘、光驱、网络文件系统等
                result.put("disk.type.name", fs.getTypeName());
                // 文件系统类型，比如 FAT32、NTFS
                result.put("disk.sys.type.name", fs.getSysTypeName());
                FileSystemUsage usage = null;
                usage = sigar.getFileSystemUsage(fs.getDirName());
                switch (fs.getType()) {
                    case 0: // TYPE_UNKNOWN ：未知
                        break;
                    case 1: // TYPE_NONE
                        break;
                    case 2: // TYPE_LOCAL_DISK : 本地硬盘
                        // 文件系统总大小
                        result.put(fs.getDevName() + "disk.total", usage.getTotal());
                        // 文件系统剩余大小
                        result.put(fs.getDevName() + "disk.free", usage.getFree());
                        // 文件系统可用大小
                        result.put(fs.getDevName() + "disk.avail", usage.getAvail());
                        // 文件系统已经使用量
                        result.put(fs.getDevName() + "disk.used", usage.getUsed());
                        double usePercent = usage.getUsePercent() * 100D;
                        // 文件系统资源的利用率
                        result.put(fs.getDevName() + "disk.use.percent", usePercent);
                        break;
                    case 3:// TYPE_NETWORK ：网络
                        break;
                    case 4:// TYPE_RAM_DISK ：闪存
                        break;
                    case 5:// TYPE_CDROM ：光驱
                        break;
                    case 6:// TYPE_SWAP ：页面交换
                        break;
                }
                result.put("disk.read", usage.getDiskReads());
                result.put("disk.write", usage.getDiskWrites());
                list.add(result);
            }
        } catch (SigarException e) {
            throw new ApiException(e);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> net() {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            Sigar sigar = new Sigar();
            String ifNames[] = sigar.getNetInterfaceList();
            for (int i = 0; i < ifNames.length; i++) {
                Map<String, Object> result = new LinkedHashMap<>();
                String name = ifNames[i];
                NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
                result.put("device.name", name);// 网络设备名
                result.put("device.address", ifconfig.getAddress());// IP地址
                result.put("device.netmask", ifconfig.getNetmask());// 子网掩码
                if ((ifconfig.getFlags() & 1L) <= 0L) {
                    continue;
                }
                NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
                result.put("rx.packets:", ifstat.getRxPackets());// 接收的总包裹数
                result.put("tx.packets:", ifstat.getTxPackets());// 发送的总包裹数
                result.put("rx.bytes:", ifstat.getRxBytes());// 接收到的总字节数
                result.put("tx.bytes:", ifstat.getTxBytes());// 发送的总字节数
                result.put("rx.errors:", ifstat.getRxErrors());// 接收到的错误包数
                result.put("tx.errors:", ifstat.getTxErrors());// 发送数据包时的错误数
                result.put("rx.dropped:", ifstat.getRxDropped());// 接收时丢弃的包数
                result.put("tx.dropped:", ifstat.getTxDropped());// 发送时丢弃的包数
                list.add(result);
            }
        } catch (SigarException e) {
            throw new ApiException(e);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> ethernet() {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            Sigar sigar = new Sigar();
            String[] ifaces = sigar.getNetInterfaceList();
            for (int i = 0; i < ifaces.length; i++) {
                NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
                if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress()) || (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0 || NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
                    continue;
                }
                Map<String, Object> result = new LinkedHashMap<>();
                result.put("ethernet.name:", cfg.getName());// IP地址
                result.put("ethernet.address:", cfg.getAddress());// IP地址
                result.put("ethernet.broadcast:", cfg.getBroadcast());// 网关广播地址
                result.put("ethernet.mac:", cfg.getHwaddr());// 网卡MAC地址
                result.put("ethernet.netmask:", cfg.getNetmask());// 子网掩码
                result.put("ethernet.description:", cfg.getDescription());// 网卡描述信息
                result.put("ethernet.type", cfg.getType());//
                list.add(result);
            }
        } catch (SigarException e) {
            throw new ApiException(e);
        }
        return list;
    }
}
