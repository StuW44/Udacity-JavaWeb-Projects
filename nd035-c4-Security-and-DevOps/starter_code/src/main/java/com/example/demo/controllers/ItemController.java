package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {
	static Logger log = Logger.getLogger(ItemController.class);

	@Autowired
	private ItemRepository itemRepository;
	
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		List<Item> items = itemRepository.findAll();
		log.info(String.format("Success :  %d items found.",items.size()));
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		Optional<Item> item= itemRepository.findById(id);
		if(!item.isPresent())
			log.error(String.format("Failure : item id %d does not exist.",id));
		else
			log.info(String.format("Success : item id %d found.",id));
		return ResponseEntity.of(item);
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);

		if( items == null || items.isEmpty()){
			log.error(String.format("Failure : item does not exist for name %s.",name));
			return  ResponseEntity.notFound().build();
		}
		else {
			log.info(String.format("Success : found %d items for name %s.",items.size(),name));
			return ResponseEntity.ok(items);
		}
	}
	
}
