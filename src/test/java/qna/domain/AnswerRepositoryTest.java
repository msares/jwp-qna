package qna.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void 답변_저장() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        assertAll(
                () -> assertThat(answer.getId()).isNotNull(),
                () -> assertThat(answer.getWriterId()).isEqualTo(AnswerTest.A1.getWriterId()),
                () -> assertThat(answer.getQuestionId()).isEqualTo(AnswerTest.A1.getQuestionId()),
                () -> assertThat(answer.getContents()).isEqualTo(AnswerTest.A1.getContents()),
                () -> assertThat(answer.isDeleted()).isFalse(),
                () -> assertThat(answer.getCreatedAt()).isNotNull(),
                () -> assertThat(answer.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void 답변_저장_후_조회() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        Answer actual = answerRepository.findById(answer.getId()).orElseThrow(EntityNotFoundException::new);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getWriterId()).isEqualTo(AnswerTest.A1.getWriterId()),
                () -> assertThat(actual.getQuestionId()).isEqualTo(AnswerTest.A1.getQuestionId()),
                () -> assertThat(actual.getContents()).isEqualTo(AnswerTest.A1.getContents()),
                () -> assertThat(actual.isDeleted()).isFalse(),
                () -> assertThat(actual.getCreatedAt()).isNotNull(),
                () -> assertThat(actual.getUpdatedAt()).isNotNull()
        );
    }

    @Test
    void 답변_삭제() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        answerRepository.deleteById(answer.getId());
        assertThat(answerRepository.findByIdAndDeletedFalse(answer.getId())).isNotPresent();
    }

    @Test
    void ID로_답변_조회() {
        answerRepository.save(AnswerTest.A1);
        List<Answer> actual = answerRepository.findByQuestionIdAndDeletedFalse(QuestionTest.Q1.getId());
        assertThat(actual).hasSize(1);
    }

    @Test
    void 삭제되지_않은_답변_조회() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        Optional<Answer> actual = answerRepository.findByIdAndDeletedFalse(answer.getId());
        assertThat(actual).isPresent();
    }

    @Test
    void 영속성_초기화후_같은객채_조회() {
        Answer answer = answerRepository.save(AnswerTest.A1);
        flushAndClear();
        Answer actual = answerRepository.findById(answer.getId()).get();
        assertThat(actual).isEqualTo(answer);
    }

    private void flushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }
}
