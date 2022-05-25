package qna.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;
import qna.NotFoundException;
import qna.UnAuthorizedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AnswerTest {
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");

    @DisplayName("Answer 도메인 생성")
    @Test
    void test_new() {
        //given & when
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents");
        //then
        assertThat(answer).isNotNull();
    }

    @DisplayName("Question 이 없을 경우 Answer 생성 불가")
    @Test
    void test_null_question() {
        //given & when & then
        assertThatThrownBy(() -> new Answer(UserTest.JAVAJIGI, null, "Answers Contents"))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("User 가 없으면 Answer 생성 불가")
    @Test
    void test_null_user() {
        //given & when & then
        assertThatThrownBy(() -> new Answer(null, QuestionTest.Q1, "Answers Contents"))
                .isInstanceOf(UnAuthorizedException.class);
    }

    @DisplayName("소유자 확인")
    @Test
    void test_is_owner() {
        //given & when & then
        assertAll(
                () -> assertThat(A1.isOwner(UserTest.JAVAJIGI)).isTrue(),
                () -> assertThat(A2.isOwner(UserTest.SANJIGI)).isTrue()
        );
    }

    @DisplayName("Answer 삭제 시 deleted 상태 업데이트")
    @Test
    void test_delete() throws CannotDeleteException {
        //given
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents");
        //when
        answer.delete(UserTest.JAVAJIGI);
        //then
        assertThat(answer.isDeleted()).isTrue();
    }

    @DisplayName("로그인 사용자가 Answer 의 작성자(writer)와 동일하지 않으면 오류")
    @Test
    void test_writer_not_equals_login_user() {
        //given
        Answer answer = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents");
        //when & then
        assertThatThrownBy(() -> answer.delete(UserTest.SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }
}
