import { IRMRequirement } from 'app/shared/model//rm-requirement.model';

export interface IRMProject {
    id?: number;
    name?: string;
    requirements?: IRMRequirement[];
}

export class RMProject implements IRMProject {
    constructor(public id?: number, public name?: string, public requirements?: IRMRequirement[]) {}
}
