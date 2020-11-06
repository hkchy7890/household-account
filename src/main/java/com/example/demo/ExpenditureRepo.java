package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenditureRepo extends JpaRepository<Expenditure, Long> {
	@Query(nativeQuery = true, value = "select sum(cost) from my_expenditure")
	int findSumCost();
	
	@Query(nativeQuery = true, value = "select category1, sum(cost) from my_expenditure group by category1")
	List<Object[]> findAllByCategory();
	
	@Query(nativeQuery = true, value = "select year(pdate), month(pdate), sum(case when category1 = '食費' then cost end) as food, sum(case when category1 = '交通費' then cost end) as traffic, sum(case when category1 = '雑費' then cost end) as others, sum(cost) as sum_total from my_expenditure group by year(pdate), month(pdate)")
	List<Object[]> findAllByMonth();
	
	@Query(nativeQuery = true, value = "select sum(cost) from my_expenditure where category1 = '食費'")
	List<Object[]> findSumFood();
	
	@Query(nativeQuery = true, value = "select sum(cost) from my_expenditure where category1 = '交通費'")
	List<Object[]> findSumTraffic();
	
	@Query(nativeQuery = true, value = "select sum(cost) from my_expenditure where category1 = '雑費'")
	List<Object[]> findSumOthers();
	
	@Query(nativeQuery = true, value = "select count(*) from my_expenditure")
	int countall();
}
