package com.freeboard03.domain.pet;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/applicationContext.xml"})
@Transactional
@Rollback
public class PetRefactoringRepositoryTest {

    @Autowired
    PetRefactoringRepository petRefactoringRepository;

    @Test
    public void insertTest() {
        PetEntity pet = PetEntity.builder().kind("CAT").name("나비").age(2).build();
        petRefactoringRepository.save(pet);

        PetEntity findPet = petRefactoringRepository.findById(pet.getId()).get();

        assertThat(pet.getId(), equalTo(findPet.getId()));
        assertThat(pet.getName(), equalTo(findPet.getName()));
        assertThat(pet.getKind(), equalTo(findPet.getKind()));
        assertThat(pet.getAge(), equalTo(findPet.getAge()));
    }

    @Test
    public void findTest() {
        final String KIND = "CAT in findTest(" + randomString() + ")";
        final int INSERT_SIZE = 10;
        insertFindAllTestData(KIND, INSERT_SIZE);

        List<PetEntity> findPets = petRefactoringRepository.findAllByKind(KIND);

        assertThat(findPets.size(), equalTo(INSERT_SIZE));
    }

    void insertFindAllTestData(String KIND, int INSERT_SIZE) {
        for (int i = 0; i < INSERT_SIZE; ++i) {
            PetEntity pet = PetEntity.builder().age(2).kind(KIND).name("Test Name").build();
            petRefactoringRepository.save(pet);
        }
    }

    @Test
    @DisplayName("pet을 저장한 뒤 해당 도큐먼트의 name을 변경하고 age를 5 더한다.")
    public void updateTest() {
        PetEntity pet = PetEntity.builder().kind("CAT").name("나비").age(0).build();
        petRefactoringRepository.save(pet);

        String updatedName = "노랑이";
        int increaseAge = 5;
        PetEntity updatedPet = PetEntity.builder().age(pet.getAge() + increaseAge).name(updatedName).kind(pet.getKind()).build();

        pet.update(updatedPet);

        petRefactoringRepository.save(pet);

        PetEntity findPet = petRefactoringRepository.findById(pet.getId()).get();
        assertThat(findPet.getName(), equalTo(updatedName));
        assertThat(findPet.getAge(), equalTo(increaseAge));
    }

    @Test
    @DisplayName("pet을 저장한 뒤 해당 도큐먼트의 name을 변경하고 inc를 이용해 age를 0으로 만든다.")
    public void updateTest2() {
        int age = 2;
        PetEntity pet = PetEntity.builder().kind("CAT").name("나비").age(age).build();
        petRefactoringRepository.save(pet);

        String updatedName = "노랑이";
        int decreaseAge = -1 * age;
        PetEntity updatedPet = PetEntity.builder().age(pet.getAge() + decreaseAge).name(updatedName).build();

        pet.update(updatedPet);

        petRefactoringRepository.save(pet);

        PetEntity findPet = petRefactoringRepository.findById(pet.getId()).get();
        assertThat(findPet.getName(), equalTo(updatedName));
        assertThat(findPet.getAge(), equalTo(0));
        assertThat(findPet.getKind(), equalTo(pet.getKind()));
    }

    @Test
    @DisplayName("Persistence Context 테스트")
    @Disabled
    public void persistenceTest() {
        int age = 2;
        PetEntity pet = PetEntity.builder().kind("CAT").name("나비").age(age).build();
        petRefactoringRepository.save(pet);

        String updatedName = "노랑이";
        int decreaseAge = -1 * age;
        PetEntity updatedPet = PetEntity.builder().age(pet.getAge() + decreaseAge).name(updatedName).build();
        updatedPet.setId(pet.getId());

        // pet과 같은 id를 가진 updatedPet을 save = update
        petRefactoringRepository.save(updatedPet);

        // update 내용이 pet에 반영되었는지 확인
        assertThat(pet.getName(), equalTo(updatedName));
        assertThat(pet.getAge(), equalTo(0));
    }


    @Test
    @DisplayName("pet을 저장한 뒤 remove를 이용해 모두 삭제한다.")
    public void deleteTest() {
        final String KIND = "CAT in findTest(" + randomString() + ")";
        final int INSERT_SIZE = 10;
        insertFindAllTestData(KIND, INSERT_SIZE);

        List<PetEntity> deletedPets = petRefactoringRepository.deleteAllByKind(KIND);

        assertThat(deletedPets.size(), equalTo(INSERT_SIZE));
    }

    @Test
    @DisplayName("pet 컬렉션을 멤버변수로 가지고 있는 pet 객체를 insert한다.")
    public void insertTest2() {
        final int SIBLING_SIZE = 5;
        PetEntity pet = PetEntity.builder().kind("DOG").age(7).name("바둑이").sibling(getPets(SIBLING_SIZE)).build();
        petRefactoringRepository.save(pet);

        PetEntity findPet = petRefactoringRepository.findById(pet.getId()).get();
        assertThat(pet.getSibling().size(), equalTo(findPet.getSibling().size()));
    }

    private List<PetEntity> getPets(int size) {
        List<PetEntity> petEntities = new ArrayList<>();
        for (int i = 1; i <= size; ++i) {
            PetEntity pet = PetEntity.builder().name("sibling" + i).age(7).kind("DOG").build();
            petEntities.add(pet);
        }
        return petEntities;
    }

    @Test
    @DisplayName("pet 컬렉션을 멤버변수로 가지고 있는 pet 객체와 멤버 변수의 M:M 관계를 유자하여 insert한다.")
    public void insertTest3() {
        final int SIBLING_SIZE = 3;
        List<PetEntity> sibling = getPets(SIBLING_SIZE);
        petRefactoringRepository.saveAll(sibling);

        updateSibling(SIBLING_SIZE, sibling);

        List<PetEntity> updatedSibling = getUpdatedPetEntities(sibling);

        for (int i = 0; i < SIBLING_SIZE; ++i) {
            List<PetEntity> thisSibling = updatedSibling.get(i).getSibling();
            List<ObjectId> thisSiblingIds = thisSibling.stream().map(pet -> pet.getId()).collect(Collectors.toList());
            assertThat(thisSiblingIds, not(contains(updatedSibling.get(i).getId())));
        }
    }

    void updateSibling(int SIBLING_SIZE, List<PetEntity> savedPets) {
        for (int i = 0; i < SIBLING_SIZE; ++i) {
            ObjectId nowPetId = savedPets.get(i).getId();
            List<PetEntity> nowSiblings = savedPets.stream().filter(pet -> pet.getId().equals(nowPetId) == false).collect(Collectors.toList());
            petRefactoringRepository.findById(nowPetId, nowSiblings);
        }
    }

    List<PetEntity> getUpdatedPetEntities(List<PetEntity> sibling) {
        return petRefactoringRepository.findAllByIdIn(sibling.stream().map(pet -> pet.getId()).collect(Collectors.toList()));
    }

    private String randomString() {
        String id = "";
        for (int i = 0; i < 10; i++) {
            double dValue = Math.random();
            if (i % 2 == 0) {
                id += (char) ((dValue * 26) + 65);   // 대문자
                continue;
            }
            id += (char) ((dValue * 26) + 97); // 소문자
        }
        return id;
    }
}
