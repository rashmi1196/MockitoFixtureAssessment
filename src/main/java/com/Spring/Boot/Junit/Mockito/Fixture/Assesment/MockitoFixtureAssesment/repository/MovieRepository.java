package com.Spring.Boot.Junit.Mockito.Fixture.Assesment.MockitoFixtureAssesment.repository;


import com.Spring.Boot.Junit.Mockito.Fixture.Assesment.MockitoFixtureAssesment.model.Movie;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends MongoRepository<Movie, ObjectId> {
}
