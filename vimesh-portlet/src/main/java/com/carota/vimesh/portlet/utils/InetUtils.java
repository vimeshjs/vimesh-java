package com.carota.vimesh.portlet.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public final class InetUtils {
    
    public static InetAddress findLocalAddress() throws SocketException, UnknownHostException {
        // find address from network interfaces
        Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
        while (networks.hasMoreElements()) {
            NetworkInterface network = networks.nextElement();
            if (!network.isUp() || network.isLoopback()) {
                continue;
            }
            
            Enumeration<InetAddress> addresses = network.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                // prefer ipv4 address
                if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                    return address;
                }
            }
        }
        
        // if address not found, back to local host
        return InetAddress.getLocalHost();
    }
}
