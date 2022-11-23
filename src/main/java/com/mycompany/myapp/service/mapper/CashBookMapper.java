package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.CashBook;
import com.mycompany.myapp.service.dto.CashBookDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link com.mycompany.myapp.domain.CashBook} and its DTO {@link com.mycompany.myapp.service.dto.CashBookDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CashBookMapper extends EntityMapper<CashBookDTO, CashBook> {
    @Mapping(source = "transactionDate", target = "date")
    @Mapping(source = "transactionAmountCR", target = "cr")
    @Mapping(source = "transactionAmountDR", target = "dr")
    @Mapping(source = "transactionDescription", target = "description")
    CashBookDTO toDto(CashBook cashBook);

    CashBook toEntity(CashBookDTO cashBookDTO);

    default CashBook fromId(Long id) {
        if (id == null) {
            return null;
        }
        CashBook cashBook = new CashBook();
        cashBook.setId(id);
        return cashBook;
    }
}
