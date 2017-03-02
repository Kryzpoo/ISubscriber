package client;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

class NetworkContainer {

    /*
     * Собирает IP всех устройств на ПК
     * Возвращает строку с IP
     */
    static String getIPAddresses() {
        String IPs = "";
        Enumeration<NetworkInterface> nets;

        try {
            nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netInt : Collections.list(nets)) {

                Enumeration<InetAddress> inetAddresses = netInt.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    IPs += inetAddress + "\n";
                }
            }
        } catch (SocketException e) {
            System.out.println("Errors with getting IP");
            e.printStackTrace();
        }
        return IPs;
    }


    /*
     * Собирает MAC-адреса всех устройств на ПК
     * Получает IP-адреса устройств
     * Возвращает строку с MAC-адресами устройств по IP
     */
    public static String getMACAdress(InetAddress IP) {
        NetworkInterface network;
        String MACAddress = "";

        try {
            network = NetworkInterface.getByInetAddress(IP);
            byte[] MAC = network.getHardwareAddress();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < MAC.length; i++) {
                stringBuilder.append(String.format("%02X%s", MAC[i],(i< MAC.length - 1)?"-":""));
            }
            MACAddress = MACAddress + stringBuilder.toString();
        } catch (SocketException e) {
            System.out.println("Errors with getting MAC");
            e.printStackTrace();
        }
        return MACAddress;
    }
}
