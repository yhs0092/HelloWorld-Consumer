package training.demo.consumer.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import com.netflix.config.DynamicLongProperty;
import com.netflix.config.DynamicPropertyFactory;

import training.demo.provider.HelloServiceIntf;

@RestSchema(schemaId = "consumer")
@Path("/consumer")
public class ConsumerService {
  private RestTemplate restTemplate = RestTemplateBuilder.create();

  @RpcReference(microserviceName = "provider", schemaId = "hello")
  private HelloServiceIntf helloService;

  private DynamicLongProperty helloDelay =
      DynamicPropertyFactory.getInstance().getLongProperty("delay.hello", 0);

  @Path("/hello")
  @GET
  public String hello(@QueryParam("name") String name) {
    if (helloDelay.get() > 0) {
      try {
        Thread.sleep(helloDelay.get());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return helloService.sayHello(name);
  }

  @Path("/helloRestTemplate")
  @GET
  public String helloRestTemplate(@QueryParam("name") String name) {
    return restTemplate.getForObject("cse://provider/hello/" + name, String.class);
  }
}
