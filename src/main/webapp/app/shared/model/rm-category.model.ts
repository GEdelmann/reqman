import { IRMRequirement } from 'app/shared/model//rm-requirement.model';

export interface IRMCategory {
    id?: number;
    name?: string;
    names?: IRMRequirement[];
}

export class RMCategory implements IRMCategory {
    constructor(public id?: number, public name?: string, public names?: IRMRequirement[]) {}
}
