package training.demo.consumer.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.apache.servicecomb.provider.pojo.RpcReference;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import training.demo.provider.HelloServiceIntf;

@RestSchema(schemaId = "consumer")
@Path("/consumer")
public class ConsumerService {
  @RpcReference(microserviceName = "provider", schemaId = "hello")
  private HelloServiceIntf helloService;

  private RestTemplate restTemplate = RestTemplateBuilder.create();

  @Path("/hello")
  @GET
  public String hello(@QueryParam("name") String name) {
    return helloService.sayHello(name);
  }

  @Path("/helloRestTemplate")
  @GET
  public String helloRestTemplate(@QueryParam("name") String name) {
    return restTemplate.getForObject("cse://provider/hello/" + name, String.class);
  }
}
