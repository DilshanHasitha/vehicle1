package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Employee;
import com.mycompany.myapp.domain.EmployeeType;
import com.mycompany.myapp.domain.Images;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.EmployeeRepository;
import com.mycompany.myapp.service.criteria.EmployeeCriteria;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SALARY = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALARY = new BigDecimal(2);
    private static final BigDecimal SMALLER_SALARY = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity(EntityManager em) {
        Employee employee = new Employee()
            .code(DEFAULT_CODE)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .isActive(DEFAULT_IS_ACTIVE)
            .phone(DEFAULT_PHONE)
            .addressLine1(DEFAULT_ADDRESS_LINE_1)
            .addressLine2(DEFAULT_ADDRESS_LINE_2)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY)
            .salary(DEFAULT_SALARY);
        return employee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity(EntityManager em) {
        Employee employee = new Employee()
            .code(UPDATED_CODE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .salary(UPDATED_SALARY);
        return employee;
    }

    @BeforeEach
    public void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployee() throws Exception {
        int databaseSizeBeforeCreate = employeeRepository.findAll().size();
        // Create the Employee
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isCreated());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate + 1);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testEmployee.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployee.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testEmployee.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testEmployee.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE_1);
        assertThat(testEmployee.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE_2);
        assertThat(testEmployee.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testEmployee.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testEmployee.getSalary()).isEqualByComparingTo(DEFAULT_SALARY);
    }

    @Test
    @Transactional
    void createEmployeeWithExistingId() throws Exception {
        // Create the Employee with an existing ID
        employee.setId(1L);

        int databaseSizeBeforeCreate = employeeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setFirstName(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = employeeRepository.findAll().size();
        // set the field null
        employee.setPhone(null);

        // Create the Employee, which fails.

        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isBadRequest());

        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmployees() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(sameNumber(DEFAULT_SALARY))));
    }

    @Test
    @Transactional
    void getEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.addressLine1").value(DEFAULT_ADDRESS_LINE_1))
            .andExpect(jsonPath("$.addressLine2").value(DEFAULT_ADDRESS_LINE_2))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.salary").value(sameNumber(DEFAULT_SALARY)));
    }

    @Test
    @Transactional
    void getEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        Long id = employee.getId();

        defaultEmployeeShouldBeFound("id.equals=" + id);
        defaultEmployeeShouldNotBeFound("id.notEquals=" + id);

        defaultEmployeeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.greaterThan=" + id);

        defaultEmployeeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEmployeeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where code equals to DEFAULT_CODE
        defaultEmployeeShouldBeFound("code.equals=" + DEFAULT_CODE);

        // Get all the employeeList where code equals to UPDATED_CODE
        defaultEmployeeShouldNotBeFound("code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where code in DEFAULT_CODE or UPDATED_CODE
        defaultEmployeeShouldBeFound("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE);

        // Get all the employeeList where code equals to UPDATED_CODE
        defaultEmployeeShouldNotBeFound("code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where code is not null
        defaultEmployeeShouldBeFound("code.specified=true");

        // Get all the employeeList where code is null
        defaultEmployeeShouldNotBeFound("code.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByCodeContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where code contains DEFAULT_CODE
        defaultEmployeeShouldBeFound("code.contains=" + DEFAULT_CODE);

        // Get all the employeeList where code contains UPDATED_CODE
        defaultEmployeeShouldNotBeFound("code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where code does not contain DEFAULT_CODE
        defaultEmployeeShouldNotBeFound("code.doesNotContain=" + DEFAULT_CODE);

        // Get all the employeeList where code does not contain UPDATED_CODE
        defaultEmployeeShouldBeFound("code.doesNotContain=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName equals to DEFAULT_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the employeeList where firstName equals to UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName is not null
        defaultEmployeeShouldBeFound("firstName.specified=true");

        // Get all the employeeList where firstName is null
        defaultEmployeeShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName contains DEFAULT_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName contains UPDATED_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where firstName does not contain DEFAULT_FIRST_NAME
        defaultEmployeeShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the employeeList where firstName does not contain UPDATED_FIRST_NAME
        defaultEmployeeShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName equals to DEFAULT_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the employeeList where lastName equals to UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName is not null
        defaultEmployeeShouldBeFound("lastName.specified=true");

        // Get all the employeeList where lastName is null
        defaultEmployeeShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName contains DEFAULT_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName contains UPDATED_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where lastName does not contain DEFAULT_LAST_NAME
        defaultEmployeeShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the employeeList where lastName does not contain UPDATED_LAST_NAME
        defaultEmployeeShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email equals to DEFAULT_EMAIL
        defaultEmployeeShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the employeeList where email equals to UPDATED_EMAIL
        defaultEmployeeShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultEmployeeShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the employeeList where email equals to UPDATED_EMAIL
        defaultEmployeeShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email is not null
        defaultEmployeeShouldBeFound("email.specified=true");

        // Get all the employeeList where email is null
        defaultEmployeeShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email contains DEFAULT_EMAIL
        defaultEmployeeShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the employeeList where email contains UPDATED_EMAIL
        defaultEmployeeShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email does not contain DEFAULT_EMAIL
        defaultEmployeeShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the employeeList where email does not contain UPDATED_EMAIL
        defaultEmployeeShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where isActive equals to DEFAULT_IS_ACTIVE
        defaultEmployeeShouldBeFound("isActive.equals=" + DEFAULT_IS_ACTIVE);

        // Get all the employeeList where isActive equals to UPDATED_IS_ACTIVE
        defaultEmployeeShouldNotBeFound("isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEmployeesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where isActive in DEFAULT_IS_ACTIVE or UPDATED_IS_ACTIVE
        defaultEmployeeShouldBeFound("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE);

        // Get all the employeeList where isActive equals to UPDATED_IS_ACTIVE
        defaultEmployeeShouldNotBeFound("isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEmployeesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where isActive is not null
        defaultEmployeeShouldBeFound("isActive.specified=true");

        // Get all the employeeList where isActive is null
        defaultEmployeeShouldNotBeFound("isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phone equals to DEFAULT_PHONE
        defaultEmployeeShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the employeeList where phone equals to UPDATED_PHONE
        defaultEmployeeShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultEmployeeShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the employeeList where phone equals to UPDATED_PHONE
        defaultEmployeeShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phone is not null
        defaultEmployeeShouldBeFound("phone.specified=true");

        // Get all the employeeList where phone is null
        defaultEmployeeShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phone contains DEFAULT_PHONE
        defaultEmployeeShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the employeeList where phone contains UPDATED_PHONE
        defaultEmployeeShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phone does not contain DEFAULT_PHONE
        defaultEmployeeShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the employeeList where phone does not contain UPDATED_PHONE
        defaultEmployeeShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine1IsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine1 equals to DEFAULT_ADDRESS_LINE_1
        defaultEmployeeShouldBeFound("addressLine1.equals=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the employeeList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultEmployeeShouldNotBeFound("addressLine1.equals=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine1IsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine1 in DEFAULT_ADDRESS_LINE_1 or UPDATED_ADDRESS_LINE_1
        defaultEmployeeShouldBeFound("addressLine1.in=" + DEFAULT_ADDRESS_LINE_1 + "," + UPDATED_ADDRESS_LINE_1);

        // Get all the employeeList where addressLine1 equals to UPDATED_ADDRESS_LINE_1
        defaultEmployeeShouldNotBeFound("addressLine1.in=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine1IsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine1 is not null
        defaultEmployeeShouldBeFound("addressLine1.specified=true");

        // Get all the employeeList where addressLine1 is null
        defaultEmployeeShouldNotBeFound("addressLine1.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine1ContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine1 contains DEFAULT_ADDRESS_LINE_1
        defaultEmployeeShouldBeFound("addressLine1.contains=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the employeeList where addressLine1 contains UPDATED_ADDRESS_LINE_1
        defaultEmployeeShouldNotBeFound("addressLine1.contains=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine1NotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine1 does not contain DEFAULT_ADDRESS_LINE_1
        defaultEmployeeShouldNotBeFound("addressLine1.doesNotContain=" + DEFAULT_ADDRESS_LINE_1);

        // Get all the employeeList where addressLine1 does not contain UPDATED_ADDRESS_LINE_1
        defaultEmployeeShouldBeFound("addressLine1.doesNotContain=" + UPDATED_ADDRESS_LINE_1);
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine2IsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine2 equals to DEFAULT_ADDRESS_LINE_2
        defaultEmployeeShouldBeFound("addressLine2.equals=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the employeeList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultEmployeeShouldNotBeFound("addressLine2.equals=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine2IsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine2 in DEFAULT_ADDRESS_LINE_2 or UPDATED_ADDRESS_LINE_2
        defaultEmployeeShouldBeFound("addressLine2.in=" + DEFAULT_ADDRESS_LINE_2 + "," + UPDATED_ADDRESS_LINE_2);

        // Get all the employeeList where addressLine2 equals to UPDATED_ADDRESS_LINE_2
        defaultEmployeeShouldNotBeFound("addressLine2.in=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine2IsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine2 is not null
        defaultEmployeeShouldBeFound("addressLine2.specified=true");

        // Get all the employeeList where addressLine2 is null
        defaultEmployeeShouldNotBeFound("addressLine2.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine2ContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine2 contains DEFAULT_ADDRESS_LINE_2
        defaultEmployeeShouldBeFound("addressLine2.contains=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the employeeList where addressLine2 contains UPDATED_ADDRESS_LINE_2
        defaultEmployeeShouldNotBeFound("addressLine2.contains=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllEmployeesByAddressLine2NotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where addressLine2 does not contain DEFAULT_ADDRESS_LINE_2
        defaultEmployeeShouldNotBeFound("addressLine2.doesNotContain=" + DEFAULT_ADDRESS_LINE_2);

        // Get all the employeeList where addressLine2 does not contain UPDATED_ADDRESS_LINE_2
        defaultEmployeeShouldBeFound("addressLine2.doesNotContain=" + UPDATED_ADDRESS_LINE_2);
    }

    @Test
    @Transactional
    void getAllEmployeesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where city equals to DEFAULT_CITY
        defaultEmployeeShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the employeeList where city equals to UPDATED_CITY
        defaultEmployeeShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllEmployeesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where city in DEFAULT_CITY or UPDATED_CITY
        defaultEmployeeShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the employeeList where city equals to UPDATED_CITY
        defaultEmployeeShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllEmployeesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where city is not null
        defaultEmployeeShouldBeFound("city.specified=true");

        // Get all the employeeList where city is null
        defaultEmployeeShouldNotBeFound("city.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByCityContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where city contains DEFAULT_CITY
        defaultEmployeeShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the employeeList where city contains UPDATED_CITY
        defaultEmployeeShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllEmployeesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where city does not contain DEFAULT_CITY
        defaultEmployeeShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the employeeList where city does not contain UPDATED_CITY
        defaultEmployeeShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllEmployeesByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where country equals to DEFAULT_COUNTRY
        defaultEmployeeShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the employeeList where country equals to UPDATED_COUNTRY
        defaultEmployeeShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllEmployeesByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultEmployeeShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the employeeList where country equals to UPDATED_COUNTRY
        defaultEmployeeShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllEmployeesByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where country is not null
        defaultEmployeeShouldBeFound("country.specified=true");

        // Get all the employeeList where country is null
        defaultEmployeeShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByCountryContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where country contains DEFAULT_COUNTRY
        defaultEmployeeShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the employeeList where country contains UPDATED_COUNTRY
        defaultEmployeeShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllEmployeesByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where country does not contain DEFAULT_COUNTRY
        defaultEmployeeShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the employeeList where country does not contain UPDATED_COUNTRY
        defaultEmployeeShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where salary equals to DEFAULT_SALARY
        defaultEmployeeShouldBeFound("salary.equals=" + DEFAULT_SALARY);

        // Get all the employeeList where salary equals to UPDATED_SALARY
        defaultEmployeeShouldNotBeFound("salary.equals=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsInShouldWork() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where salary in DEFAULT_SALARY or UPDATED_SALARY
        defaultEmployeeShouldBeFound("salary.in=" + DEFAULT_SALARY + "," + UPDATED_SALARY);

        // Get all the employeeList where salary equals to UPDATED_SALARY
        defaultEmployeeShouldNotBeFound("salary.in=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where salary is not null
        defaultEmployeeShouldBeFound("salary.specified=true");

        // Get all the employeeList where salary is null
        defaultEmployeeShouldNotBeFound("salary.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where salary is greater than or equal to DEFAULT_SALARY
        defaultEmployeeShouldBeFound("salary.greaterThanOrEqual=" + DEFAULT_SALARY);

        // Get all the employeeList where salary is greater than or equal to UPDATED_SALARY
        defaultEmployeeShouldNotBeFound("salary.greaterThanOrEqual=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where salary is less than or equal to DEFAULT_SALARY
        defaultEmployeeShouldBeFound("salary.lessThanOrEqual=" + DEFAULT_SALARY);

        // Get all the employeeList where salary is less than or equal to SMALLER_SALARY
        defaultEmployeeShouldNotBeFound("salary.lessThanOrEqual=" + SMALLER_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where salary is less than DEFAULT_SALARY
        defaultEmployeeShouldNotBeFound("salary.lessThan=" + DEFAULT_SALARY);

        // Get all the employeeList where salary is less than UPDATED_SALARY
        defaultEmployeeShouldBeFound("salary.lessThan=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesBySalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where salary is greater than DEFAULT_SALARY
        defaultEmployeeShouldNotBeFound("salary.greaterThan=" + DEFAULT_SALARY);

        // Get all the employeeList where salary is greater than SMALLER_SALARY
        defaultEmployeeShouldBeFound("salary.greaterThan=" + SMALLER_SALARY);
    }

    @Test
    @Transactional
    void getAllEmployeesByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            employeeRepository.saveAndFlush(employee);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        employee.setUser(user);
        employeeRepository.saveAndFlush(employee);
        Long userId = user.getId();

        // Get all the employeeList where user equals to userId
        defaultEmployeeShouldBeFound("userId.equals=" + userId);

        // Get all the employeeList where user equals to (userId + 1)
        defaultEmployeeShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByImagesIsEqualToSomething() throws Exception {
        Images images;
        if (TestUtil.findAll(em, Images.class).isEmpty()) {
            employeeRepository.saveAndFlush(employee);
            images = ImagesResourceIT.createEntity(em);
        } else {
            images = TestUtil.findAll(em, Images.class).get(0);
        }
        em.persist(images);
        em.flush();
        employee.addImages(images);
        employeeRepository.saveAndFlush(employee);
        Long imagesId = images.getId();

        // Get all the employeeList where images equals to imagesId
        defaultEmployeeShouldBeFound("imagesId.equals=" + imagesId);

        // Get all the employeeList where images equals to (imagesId + 1)
        defaultEmployeeShouldNotBeFound("imagesId.equals=" + (imagesId + 1));
    }

    @Test
    @Transactional
    void getAllEmployeesByTypeIsEqualToSomething() throws Exception {
        EmployeeType type;
        if (TestUtil.findAll(em, EmployeeType.class).isEmpty()) {
            employeeRepository.saveAndFlush(employee);
            type = EmployeeTypeResourceIT.createEntity(em);
        } else {
            type = TestUtil.findAll(em, EmployeeType.class).get(0);
        }
        em.persist(type);
        em.flush();
        employee.setType(type);
        employeeRepository.saveAndFlush(employee);
        Long typeId = type.getId();

        // Get all the employeeList where type equals to typeId
        defaultEmployeeShouldBeFound("typeId.equals=" + typeId);

        // Get all the employeeList where type equals to (typeId + 1)
        defaultEmployeeShouldNotBeFound("typeId.equals=" + (typeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeShouldBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].addressLine1").value(hasItem(DEFAULT_ADDRESS_LINE_1)))
            .andExpect(jsonPath("$.[*].addressLine2").value(hasItem(DEFAULT_ADDRESS_LINE_2)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(sameNumber(DEFAULT_SALARY))));

        // Check, that the count call also returns 1
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeShouldNotBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).get();
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee);
        updatedEmployee
            .code(UPDATED_CODE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .salary(UPDATED_SALARY);

        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployee.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEmployee.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testEmployee.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testEmployee.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testEmployee.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testEmployee.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testEmployee.getSalary()).isEqualByComparingTo(UPDATED_SALARY);
    }

    @Test
    @Transactional
    void putNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employee.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEmployee.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testEmployee.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testEmployee.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testEmployee.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testEmployee.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testEmployee.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testEmployee.getSalary()).isEqualByComparingTo(DEFAULT_SALARY);
    }

    @Test
    @Transactional
    void fullUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .code(UPDATED_CODE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .isActive(UPDATED_IS_ACTIVE)
            .phone(UPDATED_PHONE)
            .addressLine1(UPDATED_ADDRESS_LINE_1)
            .addressLine2(UPDATED_ADDRESS_LINE_2)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY)
            .salary(UPDATED_SALARY);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
        Employee testEmployee = employeeList.get(employeeList.size() - 1);
        assertThat(testEmployee.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testEmployee.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testEmployee.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testEmployee.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEmployee.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testEmployee.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testEmployee.getAddressLine1()).isEqualTo(UPDATED_ADDRESS_LINE_1);
        assertThat(testEmployee.getAddressLine2()).isEqualTo(UPDATED_ADDRESS_LINE_2);
        assertThat(testEmployee.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testEmployee.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testEmployee.getSalary()).isEqualByComparingTo(UPDATED_SALARY);
    }

    @Test
    @Transactional
    void patchNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employee.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployee() throws Exception {
        int databaseSizeBeforeUpdate = employeeRepository.findAll().size();
        employee.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(employee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployee() throws Exception {
        // Initialize the database
        employeeRepository.saveAndFlush(employee);

        int databaseSizeBeforeDelete = employeeRepository.findAll().size();

        // Delete the employee
        restEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
