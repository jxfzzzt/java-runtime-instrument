package edu.fdu.se.instrument;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MethodInvocationRecord {

    private Map<Integer, MethodInvocation> invocationMap;

    private List<MethodInvocation> MethodInvocationSeq;

    public MethodInvocationRecord(Map<Integer, MethodInvocation> invocationMap) {
        checkInvocationIdValid(invocationMap.keySet());
        int n = invocationMap.size();

    }

    private void checkInvocationIdValid(Set<Integer> invocationIdList) {

    }

    private void processMethodInvocationSeq() {

    }

    public boolean containsMethod(MethodInvocation invocation) {
        return false;
    }


}
