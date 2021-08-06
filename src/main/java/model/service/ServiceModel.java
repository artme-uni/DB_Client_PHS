package model.service;

import model.properties.PropertiesBundle;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceModel {
    private final HashMap<String, PropertiesBundle> servicesBundles = new HashMap<>();
    private final Map<String, ServiceInfo> servicesInfos = new HashMap<>();
    private final ArrayList<String> orderedService = new ArrayList<>();
    private final Map<String, Set<String>> accessedServices = new HashMap<>();


    public PropertiesBundle addService(String serviceName) {
        PropertiesBundle serviceInfo = new PropertiesBundle(serviceName);
        servicesBundles.put(serviceName, serviceInfo);
        servicesInfos.put(serviceName, new ServiceInfo(serviceName));

        orderedService.add(serviceName);
        return serviceInfo;
    }

    public PropertiesBundle getServiceBundle(String serviceName) {
        PropertiesBundle serviceBundle = servicesBundles.get(serviceName);
        if (serviceBundle == null) {
            throw new NoSuchElementException("Service with name " + serviceName + " does not exist");
        }
        return serviceBundle;
    }

    public ServiceInfo getServiceInfo(String serviceName) {
        ServiceInfo serviceInfo = servicesInfos.get(serviceName);
        if (serviceInfo == null) {
            throw new NoSuchElementException("Service with name " + serviceName + " does not exist");
        }
        return serviceInfo;
    }

    public List<String> getAvailableServicesNames() {
        return orderedService;
    }

    public void grantAccess(String roleName, String serviceName){
        Set<String> serviceNames = accessedServices.computeIfAbsent(roleName, k -> new HashSet<>());
        serviceNames.add(serviceName);
    }

    public List<String> getAccessedServiceNames(String roleName){
        Set<String> accessedServiceNames = accessedServices.get(roleName);
        if(accessedServiceNames == null){
            return new ArrayList<>();
        }

        return orderedService.stream()
                .filter(accessedServiceNames::contains)
                .collect(Collectors.toList());
    }
}
