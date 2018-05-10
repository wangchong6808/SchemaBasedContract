package com.jsonschema.web.representation;

import org.springframework.cloud.contract.stubrunner.*;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TestRunner {

    private StubRunnerProperties props;
    private StubDownloaderBuilderProvider provider = new StubDownloaderBuilderProvider();

    public static void main(String[] args) throws IOException {
        TestRunner runner = new TestRunner();
        runner.init();
    }

    private void init() throws IOException {
        props = new StubRunnerProperties();
        props.setWorkOffline(true);
        props.setClassifier("stubs");
        //props.setIds(new String[]{"otr-as-stubs:service-as-reservation:master:stubs", "otr-as-stubs:service-as-vehicle:master:stubs", "otr-as-stubs:service-as-order:master:stubs"});
        props.setIds(new String[]{"otr-schema:service1:1.0:sources"});
        props.setStubsPerConsumer(false);
        StubRunnerOptions stubRunnerOptions = builder().build();
        /*if (this.props.getProxyHost() != null) {
            builder.withProxy(this.props.getProxyHost(), this.props.getProxyPort());
        }
        StubRunnerOptions stubRunnerOptions = builder.build();
        BatchStubRunner batchStubRunner = new BatchStubRunnerFactory(stubRunnerOptions,
                this.provider.getOrDefaultDownloader(stubRunnerOptions),
                this.contractVerifierMessaging != null ? this.contractVerifierMessaging
                        : new NoOpStubMessages()).buildBatchStubRunner();*/
        AetherStubDownloader downloader = new AetherStubDownloader(stubRunnerOptions);
        Map<StubConfiguration, File> maps = new HashMap<>();
        for (StubConfiguration configuration : stubRunnerOptions.getDependencies()) {
            Map.Entry<StubConfiguration, File> entry = downloader.downloadAndUnpackStubJar(configuration);
            maps.put(entry.getKey(), entry.getValue());
        }

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

    private StubRunnerOptionsBuilder builder() throws IOException {
        return new StubRunnerOptionsBuilder()
                .withMinMaxPort(this.props.getMinPort(), this.props.getMaxPort())
                .withStubRepositoryRoot("")
                .withWorkOffline(this.props.isWorkOffline())
                .withStubsClassifier(this.props.getClassifier())
                .withStubs(this.props.getIds())
                .withUsername(this.props.getUsername())
                .withPassword(this.props.getPassword())
                .withStubPerConsumer(this.props.isStubsPerConsumer())
                .withConsumerName("schemaTest");
    }
}
