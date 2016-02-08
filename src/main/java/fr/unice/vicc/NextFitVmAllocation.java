package fr.unice.vicc;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Max on 08/02/2016.
 * Tu parcours tes host, et tu remplit au fur et a mesure
 * sans retourner en arriere
 */
public class NextFitVmAllocation extends VmAllocationPolicy {

    /**
     * The map to track the server that host each running VM.
     */
    private Map<Vm, Host> hoster;
    /**
     * The host where we are actually
     */
    private Integer i;

    public NextFitVmAllocation(List<? extends Host> list) {
        super(list);
        hoster = new HashMap<>();
        i = new Integer(0);
    }

    @Override
    protected void setHostList(List<? extends Host> hostList) {
        super.setHostList(hostList);
        hoster = new HashMap<>();
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        Host temp;
        for (; i < getHostList().size(); i++) {
            temp = getHostList().get(i);
            if (temp.vmCreate(vm)) {
                hoster.put(vm, temp);
                return true;
            }
        }
        return false;

    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
        if (host.vmCreate(vm)) {
            hoster.put(vm, host);
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> list) {
        return null;
    }

    @Override
    public void deallocateHostForVm(Vm vm) {
        Host host = getHost(vm);
        host.vmDestroy(vm);
    }

    @Override
    public Host getHost(Vm vm) {
        return vm.getHost();
    }

    @Override
    public Host getHost(int vmId, int userId) {
        for (Vm vm : hoster.keySet()) {
            if (vm.getId() == vmId && vm.getUserId() == userId) {
                return vm.getHost();
            }
        }
        return null;
    }
}
