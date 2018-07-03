import { IRMAttachement } from 'app/shared/model//rm-attachement.model';

export interface IRMPage {
    id?: number;
    name?: string;
    description?: string;
    attachements?: IRMAttachement[];
}

export class RMPage implements IRMPage {
    constructor(public id?: number, public name?: string, public description?: string, public attachements?: IRMAttachement[]) {}
}
