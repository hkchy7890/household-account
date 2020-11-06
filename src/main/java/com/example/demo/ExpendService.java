package com.example.demo;

import org.springframework.data.domain.Page;

public interface ExpendService {
	Page<Expenditure> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	Expenditure getExpenditureById(long id);
}
