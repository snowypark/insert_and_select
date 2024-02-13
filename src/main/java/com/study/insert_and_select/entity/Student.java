package com.study.insert_and_select.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Student {
	private String name;
	private int age;

}
