package org.openmrs.module.endtb.report;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseContextSensitiveTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@TransactionConfiguration(defaultRollback = false)
public class BaseIntegrationTest extends BaseContextSensitiveTest {

    @Before
    public void beforeBaseIntegrationTest() throws Exception {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//        when(bahmniReportsProperties.getConfigFilePath()).thenReturn(configFilePath);
//        when(bahmniReportsProperties.getOpenmrsRootUrl()).thenReturn(dbProperties.getOpenmrsRootUrl());
//        when(bahmniReportsProperties.getOpenmrsServiceUser()).thenReturn(dbProperties.getOpenmrsServiceUser());
//        when(bahmniReportsProperties.getOpenmrsServicePassword()).thenReturn(dbProperties.getOpenmrsServicePassword());
//        when(bahmniReportsProperties.getOpenmrsConnectionTimeout()).thenReturn(dbProperties.getOpenmrsConnectionTimeout());
//        when(bahmniReportsProperties.getOpenmrsReplyTimeout()).thenReturn(dbProperties.getOpenmrsReplyTimeout());
//        when(bahmniReportsProperties.getMacroTemplatesTempDirectory()).thenReturn("/tmp");
//        when(allDatasources.getConnectionFromDatasource(any(BaseReportTemplate.class))).thenReturn(getDatabaseConnection());

        setUpTestData();
        Context.authenticate("admin", "test");
    }
    private void setUpTestData() throws Exception {
        deleteAllData();
        if (!Context.isSessionOpen()) {
            Context.openSession();
        }
//        executeDataSet("datasets/initialTestDataSet.xml");
//        executeDataSet("datasets/testDataSet.xml");
        getConnection().commit();
        Context.clearSession();
    }

    @Override
    public Boolean useInMemoryDatabase() {
        return false;
    }

    @Override
    public Properties getRuntimeProperties() {

        Properties properties = new Properties();
        properties.put("connection.url", "jdbc:mysql://192.168.33.10:3306/reports_integration_tests?allowMultiQueries=true");
        properties.put("connection.username", "root");
        properties.put("connection.password", "password");
        return properties;
    }

    protected Connection getDatabaseConnection() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://192.168.33.10:3306/reports_integration_tests?allowMultiQueries=true",
                    "root" ,"password");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void test(){
        assertEquals(1,1);
    }

}
