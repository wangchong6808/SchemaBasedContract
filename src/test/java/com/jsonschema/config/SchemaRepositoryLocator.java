package com.jsonschema.config;

import com.jsonschema.util.ClasspathSchemaLoader;
import org.springframework.cloud.contract.stubrunner.*;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SchemaRepositoryLocator {

    //private StubDownloaderBuilderProvider provider = new StubDownloaderBuilderProvider();

    private static Map<String, SchemaRepository> repositoryMap = new HashMap<>();

    public static void main(String[] args) throws IOException {
        SchemaRepositoryLocator runner = new SchemaRepositoryLocator();
        runner.locate("customer");
    }

    public static SchemaRepository locate(String service) throws IOException {
        if (!repositoryMap.containsKey(service)) {
            synchronized (SchemaRepositoryLocator.class) {
                if (!repositoryMap.containsKey(service)) {
                    SchemaRepository repository = download(service);
                    repositoryMap.put(service, repository);
                }
            }
        }
        return repositoryMap.get(service);
    }
    public static SchemaRepository download(String service) throws IOException {
        Properties properties = new Properties();
        properties.load(SchemaRepositoryLocator.class.getResourceAsStream("/schema/json/provider.properties"));

        StubRunnerProperties props = new StubRunnerProperties();
        props.setWorkOffline(true);
        props.setClassifier("stubs");
        //props.setIds(new String[]{"otr-as-stubs:service-as-reservation:master:stubs", "otr-as-stubs:service-as-vehicle:master:stubs", "otr-as-stubs:service-as-order:master:stubs"});
        props.setIds(new String[]{(String) properties.get(service)});
        props.setStubsPerConsumer(false);
        StubRunnerOptions stubRunnerOptions = build(props);
        /*if (this.props.getProxyHost() != null) {
            builder.withProxy(this.props.getProxyHost(), this.props.getProxyPort());
        }
        StubRunnerOptions stubRunnerOptions = builder.build();
        BatchStubRunner batchStubRunner = new BatchStubRunnerFactory(stubRunnerOptions,
                this.provider.getOrDefaultDownloader(stubRunnerOptions),
                this.contractVerifierMessaging != null ? this.contractVerifierMessaging
                        : new NoOpStubMessages()).buildBatchStubRunner();*/
        AetherStubDownloader downloader = new AetherStubDownloader(stubRunnerOptions);
        Map<String, String> tempFilePath = new HashMap<>();
        StubConfiguration configuration = stubRunnerOptions.getDependencies().iterator().next();
        //for (StubConfiguration configuration : stubRunnerOptions.getDependencies()) {
        Map.Entry<StubConfiguration, File> entry = downloader.downloadAndUnpackStubJar(configuration);
        String path = entry.getValue().getAbsolutePath() + File.separator + "schema" + File.separator + "json" + File.separator;
        tempFilePath.put(service, path);
        Properties providerSchemaConfig = new Properties();
        providerSchemaConfig.load(new FileInputStream(path + "schema_config.properties"));

        //}
        return new SchemaRepository(new ClasspathSchemaLoader(path, providerSchemaConfig, false));
    }



    /*private StubRunnerOptionsBuilder builder() throws IOException {
        return new StubRunnerOptionsBuilder()
                .withMinMaxPort(10000, 150000)
                .withStubRepositoryRoot("")
                .withWorkOffline(true)
                .withStubsClassifier("stubs")
                .withStubs()
                .withUsername(null)
                .withPassword(null)
                .withStubPerConsumer(false)
                .withConsumerName("schemaTest");
    }*/

    private static StubRunnerOptions build(StubRunnerProperties props) {
        return new StubRunnerOptionsBuilder()
                .withMinMaxPort(props.getMinPort(), props.getMaxPort())
                .withStubRepositoryRoot("")
                .withWorkOffline(props.isWorkOffline())
                .withStubsClassifier(props.getClassifier())
                .withStubs(props.getIds())
                .withUsername(props.getUsername())
                .withPassword(props.getPassword())
                .withStubPerConsumer(props.isStubsPerConsumer())
                .withConsumerName("schemaTest").build();
    }
}
