package com.verinite.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.verinite.model.Item;
import com.verinite.repository.ItemRepository;

//Controller


import javax.persistence.EntityNotFoundException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/items")
public class ItemController {
  @Autowired
  private ItemRepository repo;

  @RequestMapping(method = RequestMethod.GET)
  public List<Item> findItems() {
    return repo.findAll();
  }

  @RequestMapping(method = RequestMethod.POST)
  public Item addItem(@RequestBody Item item) {
    //item.setId(null);
    return repo.saveAndFlush(item);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public Item updateItem(@RequestBody Item updatedItem, @PathVariable Integer id) {
    Item item = repo.getOne(id);
    item.setChecked(updatedItem.isChecked());
    item.setDescription(updatedItem.getDescription());
    return repo.saveAndFlush(item);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  public void deleteItem(@PathVariable Integer id) {
    repo.deleteById(id);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = { EmptyResultDataAccessException.class, EntityNotFoundException.class })
  public void handleNotFound() { }
}