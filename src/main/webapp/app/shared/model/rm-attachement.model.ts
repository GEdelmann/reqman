export interface IRMAttachement {
    id?: number;
    name?: string;
    mimeType?: string;
    contentContentType?: string;
    content?: any;
    rMRequirementId?: number;
    rMPageId?: number;
}

export class RMAttachement implements IRMAttachement {
    constructor(
        public id?: number,
        public name?: string,
        public mimeType?: string,
        public contentContentType?: string,
        public content?: any,
        public rMRequirementId?: number,
        public rMPageId?: number
    ) {}
}
