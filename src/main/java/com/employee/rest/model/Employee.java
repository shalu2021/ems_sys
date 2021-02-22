package com.employee.rest.model;

import com.employee.enums.EmployeeStatusEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "VARCHAR", length = 100,unique = true)
	private String name;

	@Column(columnDefinition = "integer", length = 100)
	private Integer salary;

	@Column(columnDefinition = "VARCHAR", length = 100)
	private String department;

	@Column(columnDefinition = "integer", length = 100)
	private Integer age;

	@Column(columnDefinition = "VARCHAR", length = 100)
	private String city;

	@Column(columnDefinition = "VARCHAR", length = 100)
	private String state;

	@Column(columnDefinition = "integer", length = 100)
	private Integer zip;

	@Column(columnDefinition = "VARCHAR", length = 100)
	private String job_title;

	@Column(columnDefinition = "VARCHAR", length = 100)
	private String company_name;

	@Column(columnDefinition = "Date", length = 100)
	private Date birthdate;

	@Column(columnDefinition = "VARCHAR", length = 100)
	private String phoneNumber;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "VARCHAR", length = 100)
	private EmployeeStatusEnum status;


}