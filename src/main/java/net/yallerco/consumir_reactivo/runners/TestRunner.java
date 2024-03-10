package net.yallerco.consumir_reactivo.runners;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import net.yallerco.consumir_reactivo.model.Pais;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TestRunner implements CommandLineRunner{

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		
		WebClient webClient =  WebClient.create("http://localhost:8034");
		Flux<Pais> flux = webClient
		.get()
		.uri("/pais2/listar")
		.accept(MediaType.APPLICATION_JSON)
		.retrieve()
		.bodyToFlux(Pais.class);
		flux.subscribe(p->System.out.println(p));
		
		//crear
		
		WebClient webClient2 =  WebClient.create("http://localhost:8034");
		webClient2
		.post()
		.uri("/pais2/guardar")
		.body(Mono.just(new Pais(21L,"España","Madrid","ES")), Pais.class)
		.retrieve()
		.bodyToMono(Void.class)
		.doOnTerminate(()->System.out.println("Se ha creado un nuevo Registro"))
		.block();
		
		//buscar por id
		WebClient webClient3 =  WebClient.create("http://localhost:8034");
		Mono<Pais> monoBuscar = webClient3
		.get()
		.uri("/pais2/15")
		.accept(MediaType.APPLICATION_JSON)
		.retrieve()
		.bodyToMono(Pais.class);
		
		monoBuscar.subscribe(p->System.out.println(p));
		monoBuscar.switchIfEmpty(Mono.just(new Pais()).map(p->
		{
			System.out.println("No se ha encontrado el producto");
			return p;
		}
		)).block();
		
	}

}
