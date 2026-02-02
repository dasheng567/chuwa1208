## Spring Core / DI
- `@Component` — marks a class as Spring-managed bean.
- `@Service` — specialization of `@Component` for service layer.
- `@Repository` — specialization of `@Component` for DAO/repo layer; also translates persistence exceptions.
- `@Autowired` — dependency injection (field/constructor/setter). (Prefer constructor injection.)
- `@Qualifier("beanName")` — choose which bean to inject when multiple exist.
- `@Value("${property.key}")` — inject a value from properties.
- `@Configuration` — Java config class.
- `@Bean` — define a bean in a `@Configuration` class.

## Spring Boot
- `@SpringBootApplication` — = `@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`.

## Web / REST (Spring MVC)
- `@RestController` — controller + `@ResponseBody` (returns JSON by default).
- `@Controller` — MVC controller (often returns views).
- `@RequestMapping("/path")` — base mapping for controller or method.
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PatchMapping` — HTTP method mappings.
- `@PathVariable` — bind `{id}` from URL.
- `@RequestParam` — bind `?key=value` query parameter.
- `@RequestBody` — bind JSON body to Java object.
- `@ResponseStatus(HttpStatus.XXX)` — set HTTP status for response.
- `@CrossOrigin` — allow CORS.

## Validation (Jakarta Validation)
- `@Valid` — trigger validation on request DTO.
- `@NotNull`, `@NotBlank`, `@NotEmpty` — common required validations.
- `@Size(min=, max=)` — string/collection size constraints.
- `@Min`, `@Max`, `@Positive`, `@PositiveOrZero` — numeric constraints.
- `@Email` — email format constraint.

## JPA / Hibernate (Entities)
- `@Entity` — JPA entity (mapped to a table).
- `@Table(name="...")` — customize table name.
- `@Id` — primary key.
- `@GeneratedValue(strategy=...)` — auto-generate PK value.
- `@Column(name="...", nullable=..., unique=..., length=...)` — customize column.
- `@Transient` — not persisted to DB.
- `@Enumerated(EnumType.STRING)` — map enum to string.
- `@CreationTimestamp`, `@UpdateTimestamp` (Hibernate) — auto timestamps.

## Relationships
- `@OneToMany(mappedBy="...")`
- `@ManyToOne`
- `@ManyToMany`
- `@JoinColumn(name="...")` — FK column
- `@JoinTable(name="...", joinColumns=..., inverseJoinColumns=...)` — join table for many-to-many

## Lombok
- `@Getter`, `@Setter`
- `@NoArgsConstructor`, `@AllArgsConstructor`, `@RequiredArgsConstructor`
- `@ToString`, `@EqualsAndHashCode`
- `@Data` — getter/setter/toString/equals/hashCode
- `@Builder`

## Testing
- `@SpringBootTest`
- `@WebMvcTest`
- `@MockBean`
- `@Test`
