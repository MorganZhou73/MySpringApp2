package com.unistar.myservice3;

import org.modelmapper.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class Myservice3Application {

	public static void main(String[] args) {
		SpringApplication.run(Myservice3Application.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		Provider<Date> javaDateProvider = new AbstractProvider<Date>() {
			@Override
			public java.util.Date get () {
				return new java.util.Date ();
			}
		};

		Converter<String, Date> stringToDate = new AbstractConverter<String, Date>() {
			@Override
			protected Date convert (String source) {
				if(source == null || source.isEmpty())
					return null;

				try {
					return dateFormat.parse(source);
				} catch (ParseException e) {
					e.printStackTrace ();
				}
				return null;
			}
		};

		Converter<Date, String> dateToString = new AbstractConverter<Date, String>() {
			@Override
			protected String convert(Date source) {
				if(source == null)
					return "";

				return dateFormat.format(source);
			}
		};

		mapper.createTypeMap (String.class, java.util.Date.class);
		mapper.addConverter (stringToDate);
		mapper.getTypeMap (String.class, java.util.Date.class).setProvider (javaDateProvider);
		mapper.addConverter (dateToString);
		mapper.getConfiguration().setAmbiguityIgnored(true);

		return mapper;
	}

}
