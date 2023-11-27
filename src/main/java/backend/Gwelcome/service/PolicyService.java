package backend.Gwelcome.service;

import backend.Gwelcome.dto.policy.MyPolicyResponseDTO;
import backend.Gwelcome.dto.policy.PolicyRegisterDto;
import backend.Gwelcome.dto.policy.PolicyResponseDTO;
import backend.Gwelcome.dto.policy.ReplySaveRequestDto;
import backend.Gwelcome.exception.ErrorCode;
import backend.Gwelcome.exception.GwelcomeException;
import backend.Gwelcome.model.Member;
import backend.Gwelcome.model.Policy;
import backend.Gwelcome.model.Reply;
import backend.Gwelcome.repository.MemberRepository;
import backend.Gwelcome.repository.PolicyRepository;
import backend.Gwelcome.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final ReplyRepository replyRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void uploadPolicy(PolicyRegisterDto policyRegisterDto) {
        Policy policy = Policy.builder()
                .additional_clues(policyRegisterDto.getAdditional_clues())
                .age(policyRegisterDto.getAge())
                .application_site(policyRegisterDto.getApplication_site())
                .business_application_period(policyRegisterDto.getBusiness_application_period())
                .business_operation_period(policyRegisterDto.getBusiness_operation_period())
                .documents(policyRegisterDto.getDocuments())
                .extraInfo(policyRegisterDto.getExtraInfo())
                .host_organization(policyRegisterDto.getHost_organization())
                .job_state(policyRegisterDto.getJob_state())
                .judge_presentation(policyRegisterDto.getJudge_presentation())
                .living_income(policyRegisterDto.getLiving_income())
                .major(policyRegisterDto.getMajor())
                .operating_organization(policyRegisterDto.getOperating_organization())
                .partition_restrict_apply(policyRegisterDto.getPartition_restrict_apply())
                .introduction(policyRegisterDto.getIntroduction())
                .name(policyRegisterDto.getName())
                .reference_site(policyRegisterDto.getReference_site())
                .request_procedure(policyRegisterDto.getRequest_procedure())
                .specialization(policyRegisterDto.getSpecialization())
                .university(policyRegisterDto.getUniversity())
                .useful_info(policyRegisterDto.getUseful_info())
                .photo_url(policyRegisterDto.getPhoto_url())
                .support_scale(policyRegisterDto.getSupport_scale())
                .build();
        policyRepository.save(policy);
    }

    @Transactional
    public void replyWrite(ReplySaveRequestDto replySaveRequestDto, String userId,Long policyId) {
        Member member = memberRepository.findById(userId).orElseThrow(()-> new GwelcomeException(ErrorCode.MEMBER_NOT_FOUND));
        Policy policy = policyRepository.findById(policyId).orElseThrow(()-> new GwelcomeException(ErrorCode.POLICY_NOT_FOUND));
        Reply reply = Reply.builder()
                .content(replySaveRequestDto.getContent())
                .member(member)
                .policy(policy)
                .build();
        replyRepository.save(reply);
    }

    public Page<PolicyResponseDTO> list(Pageable pageable) {
        Page<Policy> policyList = policyRepository.findAll(pageable);
        Page<PolicyResponseDTO> result = policyList.map(m->PolicyResponseDTO.builder()
                .id(m.getId())
                .title(m.getName())
                .intro(m.getIntroduction())
                .image_url(m.getPhoto_url())
                .build());
        return result;
    }
}