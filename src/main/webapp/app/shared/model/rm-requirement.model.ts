import { IRMAttachement } from 'app/shared/model//rm-attachement.model';
import { IRMTag } from 'app/shared/model//rm-tag.model';
import { IRMCategory } from 'app/shared/model//rm-category.model';

export const enum RMRequirementScope {
    REQUIRED = 'REQUIRED',
    NICETOHAVE = 'NICETOHAVE',
    OPTIONAL = 'OPTIONAL'
}

export const enum RMRequirementType {
    BUSINESS = 'BUSINESS',
    TECHNICAL = 'TECHNICAL'
}

export interface IRMRequirement {
    id?: number;
    functionalID?: string;
    headline?: string;
    description?: string;
    generalNote?: string;
    technicalNotes?: string;
    scope?: RMRequirementScope;
    type?: RMRequirementType;
    attachements?: IRMAttachement[];
    projectId?: number;
    tags?: IRMTag[];
    categories?: IRMCategory[];
}

export class RMRequirement implements IRMRequirement {
    constructor(
        public id?: number,
        public functionalID?: string,
        public headline?: string,
        public description?: string,
        public generalNote?: string,
        public technicalNotes?: string,
        public scope?: RMRequirementScope,
        public type?: RMRequirementType,
        public attachements?: IRMAttachement[],
        public projectId?: number,
        public tags?: IRMTag[],
        public categories?: IRMCategory[]
    ) {}
}
