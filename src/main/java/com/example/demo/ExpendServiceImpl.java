package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ExpendServiceImpl implements ExpendService {
	@Autowired
	private ExpenditureRepo repo;
	
	@Override
	public Page<Expenditure> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
				
		Pageable pageable = PageRequest.of(pageNo - 1 , pageSize, sort);
		return repo.findAll(pageable);
	}

	@Override
	public Expenditure getExpenditureById(long id) {
		Optional<Expenditure> optional = repo.findById(id);
		Expenditure expenditure = null;
		if (optional.isPresent()) {
			expenditure = optional.get();
		} else {
			throw new RuntimeException("404 record not found");
		}
		return expenditure;
	}

}
