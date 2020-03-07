package eu.ha3.matmos.data.modules;

import eu.ha3.matmos.core.sheet.DataPackage;
import eu.ha3.matmos.core.sheet.VirtualSheet;

/**
 * Call commit() to apply the changes.
 *
 * @author Hurry
 */
public class ExternalStringCountModule extends AbstractStringCountModule {
    public ExternalStringCountModule(DataPackage data, String name) {
        super(data, name);
    }

    public ExternalStringCountModule(DataPackage data, String name, boolean doNotUseDelta) {
        super(data, name, doNotUseDelta);
    }

    @Override
    public void process() {
        // Don't use
    }

    @Override
    public void doProcess() {
        // Don't use
    }

    @Override
    public void count() {
        // Don't use
    }

    protected void commit() {
        apply();
        if (sheet instanceof VirtualSheet) {
            ((VirtualSheet)sheet).apply();
        }
    }
}
