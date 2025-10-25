import { ChallengerPart } from '@api/types/common';
import { ProjectMember, ProjectTo } from '@api/types/project';

interface PartCurrentAndMaxTo {
  part: ChallengerPart;
  currentTo: number;
  maxTo: number;
}

export const calculateCurrentAndMaxProjectTo = (
  projectTos: ProjectTo[],
  projectMembers: ProjectMember[],
): PartCurrentAndMaxTo[] => {
  return projectTos.map((projectTo) => {
    const currentTo = projectMembers.filter((member) => member.part === projectTo.part).length;
    return {
      part: projectTo.part,
      currentTo,
      maxTo: projectTo.toCount,
    };
  });
};
