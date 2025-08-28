package com.app.entities;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "events")
@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Event extends BaseEntity {
	@Column(length = 30)
	private String name;
	@Column(length = 30)
	private String location;
	@Column(length = 100)
	private String description;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate eventDate;
	private Integer capacity;
	 
}
