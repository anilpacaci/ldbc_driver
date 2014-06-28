package com.ldbc.driver.control;

import com.ldbc.driver.util.MapUtils;
import com.ldbc.driver.util.TestUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ConfigurationFileCreatorReaderHelper {
    public static void main(String[] args) throws IOException, DriverConfigurationException {
        File driverRootDirectory = getDriverRootDirectory();

        File baseConfigurationFilePublicLocation = getBaseConfigurationFilePublicLocation(driverRootDirectory);
        createBaseConfigurationAt(baseConfigurationFilePublicLocation);
        assertThat(readConfigurationFileAt(baseConfigurationFilePublicLocation), equalTo(baseConfiguration()));

        File baseConfigurationFilePublicTestResourcesLocation = getBaseConfigurationFilePublicTestResourcesLocation(driverRootDirectory);
        createBaseConfigurationAt(baseConfigurationFilePublicTestResourcesLocation);
        assertThat(readConfigurationFileAt(baseConfigurationFilePublicTestResourcesLocation), equalTo(baseConfiguration()));

        assertThat(
                readConfigurationFileAt(baseConfigurationFilePublicLocation),
                equalTo(readConfigurationFileAt(baseConfigurationFilePublicTestResourcesLocation)));
    }

    public static void createBaseConfigurationAt(File baseConfigurationFile) throws IOException, DriverConfigurationException {
        // Delete old configuration file and create new one, in appropriate directory
        if (baseConfigurationFile.exists())
            FileUtils.deleteQuietly(baseConfigurationFile);
        baseConfigurationFile.createNewFile();

        // Create base default configuration
        ConsoleAndFileDriverConfiguration defaultsOnly = baseConfiguration();

        // Write configuration to file
        new FileOutputStream(baseConfigurationFile).write(defaultsOnly.toPropertiesString().getBytes());

        System.out.println("New configuration file written to " + baseConfigurationFile.getAbsolutePath());
    }

    public static ConsoleAndFileDriverConfiguration readConfigurationFileAt(File configurationFile) throws IOException, DriverConfigurationException {
        assertThat(configurationFile.exists(), is(true));
        Properties ldbcDriverDefaultConfigurationProperties = new Properties();
        ldbcDriverDefaultConfigurationProperties.load(new FileInputStream(configurationFile));
        Map<String, String> ldbcDriverDefaultConfigurationAsParamsMap =
                ConsoleAndFileDriverConfiguration.convertLongKeysToShortKeys(
                        MapUtils.<String, String>propertiesToMap(ldbcDriverDefaultConfigurationProperties)
                );

        if (false == ldbcDriverDefaultConfigurationAsParamsMap.containsKey(ConsoleAndFileDriverConfiguration.OPERATION_COUNT_ARG))
            ldbcDriverDefaultConfigurationAsParamsMap.put(ConsoleAndFileDriverConfiguration.OPERATION_COUNT_ARG, "0");

        return ConsoleAndFileDriverConfiguration.fromParamsMapWithoutChecks(ldbcDriverDefaultConfigurationAsParamsMap);
    }

    public static File getBaseConfigurationFilePublicLocation() {
        return getBaseConfigurationFilePublicLocation(getDriverRootDirectory());
    }

    private static ConsoleAndFileDriverConfiguration baseConfiguration() throws DriverConfigurationException {
        String databaseClassName = null;
        String workloadClassName = null;
        long operationCount = 0;
        return ConsoleAndFileDriverConfiguration.fromDefaultsWithoutChecks(databaseClassName, workloadClassName, operationCount);
    }

    private static File getDriverRootDirectory() {
        File targetDirectory = TestUtils.getResource("/");
        while (false == "target".equals(targetDirectory.getName())) {
            targetDirectory = targetDirectory.getParentFile();
        }
        // root is one step up from "target/"
        return targetDirectory.getParentFile();
    }

    private static File getBaseConfigurationFilePublicLocation(File driverRootDirectory) {
        File workloadsDirectory = new File(driverRootDirectory, "workloads");
        assertThat(workloadsDirectory.exists(), is(true));
        return new File(workloadsDirectory, "ldbc_driver_default.properties");
    }

    private static File getBaseConfigurationFilePublicTestResourcesLocation(File driverRootDirectory) throws IOException {
        File testResourcesDirectory = new File(driverRootDirectory, "src/test/resources");
        assertThat(testResourcesDirectory.exists(), is(true));
        return new File(testResourcesDirectory, "ldbc_driver_default.properties");
    }
}
