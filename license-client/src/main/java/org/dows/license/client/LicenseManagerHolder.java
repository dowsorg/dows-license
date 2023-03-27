package org.dows.license.client;

import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseParam;
import org.dows.license.api.CustomLicenseManager;

public class LicenseManagerHolder {

    private static volatile LicenseManager LICENSE_MANAGER;

    public static LicenseManager getInstance(LicenseParam param) {
        if (LICENSE_MANAGER == null) {
            synchronized (LicenseManagerHolder.class) {
                if (LICENSE_MANAGER == null) {
                    LICENSE_MANAGER = new CustomLicenseManager(param);
                }
            }
        }

        return LICENSE_MANAGER;
    }

}
