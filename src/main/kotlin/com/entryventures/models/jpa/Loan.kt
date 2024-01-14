package com.entryventures.models.jpa

import com.entryventures.models.LoanStatus
import com.entryventures.models.dto.LoanDto
import com.entryventures.repository.ClientRepository
import com.entryventures.services.Crud
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "loans")
class Loan() {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "VARCHAR(36)")
    lateinit var id: String

    @JsonProperty("amount")
    var amount: Int = 0

    @Column(name = "status", nullable = false)
    @JsonProperty("status")
    @NotNull
    var status: LoanStatus = LoanStatus.Pending

    //  A loan has many collections .
    // One-to-many relationship with Collection
    @OneToMany(mappedBy = "loan")
    @JsonIgnore
    var collections: List<Collection> = listOf()

    // one loan belongs to one client
    // One-to-one relationship with client
    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnore
    lateinit var client: Client

    constructor(loanDto: LoanDto, clientRepository: ClientRepository) : this() {
        this.amount = loanDto.amount
        this.client = Crud.find {
            clientRepository.findById(loanDto.clientId)
        }
    }

    fun toLoanDto(): LoanDto {
        return LoanDto(
            id = id,
            status = status,
            amount = amount,
            clientId = client.id
        )
    }
}