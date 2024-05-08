package com.udacity.pricing.domain.price;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.server.PathParam;
import java.util.Optional;

@RepositoryRestResource(path = "price")
public interface PriceRepository  extends CrudRepository<Price, Long> {
}
