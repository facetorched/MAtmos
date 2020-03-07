package eu.ha3.matmos.data.modules;

import java.util.Map.Entry;

import eu.ha3.matmos.core.sheet.DataPackage;

// XXX: This is a terrible use of extends.
public class ThousandStringCountModule extends ExternalStringCountModule {
    private int total = 0;

    public ThousandStringCountModule(DataPackage data, String name, boolean doNotUseDelta) {
        super(data, name, doNotUseDelta);
    }

    public ThousandStringCountModule(DataPackage data, String name) {
        super(data, name);
    }

    @Override
    public void increment(String name) {
        super.increment(name);
        total++;
    }

    @Override
    public void apply() {
        for (Entry<String, Integer> entry : things.entrySet()) {
            entry.setValue((int)Math.ceil((float)entry.getValue() / total * 1000f));
        }
        super.apply();
        total = 0;
    }
}
