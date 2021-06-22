package com.verinite.test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


import com.google.gson.Gson;
import com.verinite.controller.ItemController;
import com.verinite.model.Item;
import com.verinite.repository.ItemRepository;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
@RunWith(MockitoJUnitRunner.class)
public class ItemTests {
  private static final int CHECKED_ITEM_ID = 1;
  private static final Item CHECKED_ITEM = new ItemBuilder()
    .id(CHECKED_ITEM_ID)
    .checked()
    .build();
  private static final Item UNCHECKED_ITEM = new ItemBuilder()
    .id(2)
    .checked()
    .build();
  private static final Item NEW_ITEM = new ItemBuilder()
    .checked()
    .build();
  @InjectMocks
  private ItemController controller;
  @Mock
  private ItemRepository repository;
  private ArgumentCaptor<Item> anyItem = ArgumentCaptor.forClass(Item.class);
  
  @Test
  public void whenFindingItemsItShouldReturnAllItems() {

    given(repository.findAll()).willReturn(Arrays.asList(CHECKED_ITEM, UNCHECKED_ITEM));

    assertThat(controller.findItems())
 
    .containsOnly(CHECKED_ITEM, UNCHECKED_ITEM);
  }

  @Test
  public void whenAddingItemItShouldReturnTheSavedItem() {

    given(repository.saveAndFlush(NEW_ITEM)).willReturn(CHECKED_ITEM);

    assertThat(controller.addItem(NEW_ITEM))
   
    .isSameAs(CHECKED_ITEM);
  }
  
 
  @Test
  public void whenUpdatingItemItShouldReturnTheSavedItem() {

    given(repository.getOne(CHECKED_ITEM_ID)).willReturn(CHECKED_ITEM);

    given(repository.saveAndFlush(CHECKED_ITEM)).willReturn(CHECKED_ITEM);
   
    assertThat(controller.updateItem(CHECKED_ITEM, CHECKED_ITEM_ID))

    .isSameAs(CHECKED_ITEM);
  }
  
  //RestAssured get All Item
	/*
	 * @Test public void testItemList() { Response resp=(Response)
	 * RestAssured.given().when().get(
	 * "http://demo3-env.eba-vnbfjk9q.us-east-2.elasticbeanstalk.com/items").then().
	 * extract().response(); assertEquals(200, resp.getStatusCode()); }
	 * 
	 * //Save Data to Item
	 * 
	 * @Test public void testSaveItem() { Item item=new Item(); item.setId(3);
	 * item.setChecked(true); item.setDescription("data");
	 * 
	 * Response response =
	 * RestAssured.given().contentType(ContentType.JSON).header("Content-Type",
	 * ContentType.JSON) .body(new Gson().toJson(item)).post(
	 * "http://demo3-env.eba-vnbfjk9q.us-east-2.elasticbeanstalk.com/items").then().
	 * extract().response();
	 * 
	 * assertEquals(200, response.getStatusCode()); }
	 * 
	 * //Update to Item
	 * 
	 * @Test public void testupdateItem() { Item item=new Item(); item.setId(3);
	 * item.setChecked(false); item.setDescription("sdsdsdas");
	 * 
	 * Response response = RestAssured.given().baseUri(
	 * "http://demo3-env.eba-vnbfjk9q.us-east-2.elasticbeanstalk.com/items/1").
	 * header("Content-Type", ContentType.JSON) .body(new
	 * Gson().toJson(item)).when().put().then().extract().response(); // Response
	 * resp=(Response)
	 * RestAssured.given().when().get("http://localhost:8086/items/").then().extract
	 * ().response();
	 * 
	 * assertEquals(200, response.getStatusCode()); }
	 */
  
  @Test
  public void whenUpdatingItemItShouldUseTheGivenID() {
   
    given(repository.getOne(CHECKED_ITEM_ID)).willReturn(CHECKED_ITEM);

    controller.updateItem(NEW_ITEM, CHECKED_ITEM_ID);

    verify(repository).saveAndFlush(anyItem.capture());
   
    assertThat(anyItem.getValue().getId()).isEqualTo(CHECKED_ITEM_ID);
  }
  
  @Test
  public void whenDeletingAnItemItShouldUseTheRepository() {
 
    controller.deleteItem(CHECKED_ITEM_ID);
   
    verify(repository).deleteById(CHECKED_ITEM_ID);
  }
}
