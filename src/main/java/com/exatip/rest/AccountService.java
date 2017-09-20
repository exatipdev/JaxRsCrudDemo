package com.exatip.rest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.exatip.constant.Constant;
import com.exatip.custom.Patch;
import com.exatip.pojo.Account;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

@Path(Constant.BASE_URL)
public class AccountService {

	private static File jsonFile = new File(Constant.JSON_FILEPATH);
	private static final Logger logger = Logger.getLogger(AccountService.class);

	/**
	 * Get the Account Details by its id
	 * 
	 * @param accountId
	 *            Account id of the Account
	 * @return Account object found by its accountId
	 */
	@GET
	@Path("{accountId}")
	@Produces("application/json")
	public Account getAccount(@PathParam("accountId") String accountId) {
		try {
			logger.info("getAccount method started");

			List<Account> accountList = getAllAccounts();
			if (accountList != null) {
				return (Account) accountList.stream()
						.filter(acc -> acc.getAccountId().equals(accountId))
						.collect(Collectors.toList()).get(0);
			} else {
				logger.error(Constant.NO_OBJECT_FOUND_MESSAGE);
				return null;
			}
		} catch (Exception e) {
			logger.error(Constant.SOMETHING_WENT_WRONG_MESSAGE);
			return null;
		}

	}

	/**
	 * Create Account and write the data in JSON file
	 * 
	 * @param account
	 *            Account object
	 * @return success message
	 * @throws Exception
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@POST
	@Consumes("application/json")
	public Response createAccount(Account account) throws Exception,
			JsonMappingException, IOException {
		try {
			logger.info("createAccount method started");
			List<Account> accountList = getAllAccounts();
			if (accountList == null) {
				accountList = new ArrayList<Account>();
			}
			accountList.add(account);
			ObjectMapper writeMapper = new ObjectMapper();
			writeMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile,
					accountList);

		} catch (UnrecognizedPropertyException e) {
			logger.error(Constant.WRONG_JSON_FORMATE_MESSAGE);
			return Response.status(200)
					.entity(Constant.WRONG_JSON_FORMATE_MESSAGE).build();
		} catch (Exception e) {
			logger.error(Constant.CREATE_FAIL_MESSAGE);
			return Response.status(200).entity(Constant.CREATE_FAIL_MESSAGE)
					.build();

		}
		logger.info(Constant.CREATE_MESSAGE);
		logger.info("createAccount method ends");
		return Response.status(200).entity(Constant.CREATE_MESSAGE).build();
	}

	/***
	 * Get list of all the accounts
	 * 
	 * @return List of accounts
	 */
	@GET
	@Produces("application/json")
	public List<Account> getAllAccounts() {
		logger.info("getAllAccounts method started");
		ObjectMapper mapper = new ObjectMapper();
		List<Account> accountList = null;
		try {
			accountList = mapper.readValue(jsonFile,
					new TypeReference<List<Account>>() {
					});
		} catch (JsonParseException e) {
			logger.error("Something went wrong while parsing json");
		} catch (JsonMappingException e) {
			logger.error("Something went wrong while mapping json");
		} catch (IOException e) {
			logger.error("Something went wrong while reading json");
		}
		logger.info("getAllAccounts method ends");
		return accountList;
	}

	/**
	 * Update the account with given id
	 * 
	 * @param account
	 *            Account object needs to modify
	 * @return success message
	 */
	@Patch
	@Consumes("application/json")
	public Response updateAcccount(Account account) {
		logger.info("updateAcccount method started");
		try {
			List<Account> accountList = null;
			accountList = getAllAccounts();
			if (accountList != null) {
				OptionalInt indexOpt;
				try {
					indexOpt = IntStream
							.range(0, accountList.size())
							.filter(k -> account.getAccountId().equals(
									getAllAccounts().get(k).getAccountId()))
							.findAny();

					if (OptionalInt.empty() != null) {
						accountList.set(indexOpt.getAsInt(), account);
						ObjectMapper writeMapper = new ObjectMapper();
						try {
							writeMapper.writerWithDefaultPrettyPrinter()
									.writeValue(jsonFile, accountList);
							logger.info(Constant.UPDATE_MESSAGE);
						} catch (JsonGenerationException e) {
							logger.error("Something went wrong while creating json");
						} catch (JsonMappingException e) {
							logger.error("some thing went wrong while mapping json");
						} catch (IOException e) {
							logger.error("Something went wrong while reading json ");
						}
						logger.info("updateAcccount method ends");
						return Response.status(200)
								.entity(Constant.UPDATE_MESSAGE).build();
					} else {
						return Response.status(200)
								.entity(Constant.NO_OBJECT_FOUND_MESSAGE)
								.build();
					}
				} catch (NoSuchElementException e) {
					logger.error(Constant.NO_DATA_IN_ACCOUNT_LIST_MESSAGE);
					return Response.status(200)
							.entity(Constant.NO_DATA_IN_ACCOUNT_LIST_MESSAGE)
							.build();
				}

			}
		} catch (Exception e) {
			logger.error(Constant.SOMETHING_WENT_WRONG_MESSAGE);
		}
		logger.info("updateAcccount method ends");
		return null;

	}
	/***
	 * This service is to get meta-data
	 * 
	 * @return List of meta data
	 */
	@GET
	@Produces("application/json")
	@Path("metadata")
	public List getMetaData() {
		logger.info("getMetaData method started");
		ObjectMapper mapper = new ObjectMapper();
		ClassLoader classLoader = getClass().getClassLoader();
		List metadataList = null;
		File file = new File(classLoader.getResource("meta-data.json").getFile());
		
		try {
			metadataList = mapper.readValue(file,
					new TypeReference<List>() {
					});
		} catch (JsonParseException e) {
			logger.error("Something went wrong while parsing json");
		} catch (JsonMappingException e) {
			logger.error("Something went wrong while mapping json");
		} catch (IOException e) {
			logger.error("Something went wrong while reading json");
		}
		logger.info("getMetaData method ends");
		return metadataList;
	}
}