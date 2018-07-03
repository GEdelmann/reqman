import { IRMRequirement } from 'app/shared/model//rm-requirement.model';

export const enum RMTagType {
    GENERATED = 'GENERATED',
    MANUAL = 'MANUAL',
    LOCKED = 'LOCKED'
}

export interface IRMTag {
    id?: number;
    name?: string;
    type?: RMTagType;
    names?: IRMRequirement[];
}

export class RMTag implements IRMTag {
    constructor(public id?: number, public name?: string, public type?: RMTagType, public names?: IRMRequirement[]) {}
}
