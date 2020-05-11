package net.kuper.tz.active.service;

import java.util.List;
import java.util.Map;

public interface EnvService {

    /**
     * 项目信息
     *
     * @return
     */
    Map<String, Object> application();

    /**
     * 操作系统
     *
     * @return
     */
    Map<String, Object> os();

    /**
     * 环境信息
     *
     * @return
     */
    Map<String, Object> vmMemory();

    /**
     * 内存信息
     *
     * @return
     */
    Map<String, Object> memory();

    /**
     * CPU信息
     *
     * @return
     */
    List<Map<String, Object>> cpu();

    /**
     * 磁盘信息
     *
     * @return
     */
    List<Map<String, Object>> disk();

    /**
     * 网络信息
     *
     * @return
     */
    List<Map<String, Object>> net();

    /**
     * 网卡信息
     *
     * @return
     */
    List<Map<String, Object>> ethernet();
}
