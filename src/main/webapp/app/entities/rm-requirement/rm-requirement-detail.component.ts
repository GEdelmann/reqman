import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRMRequirement } from 'app/shared/model/rm-requirement.model';

@Component({
    selector: '-rm-requirement-detail',
    templateUrl: './rm-requirement-detail.component.html'
})
export class RMRequirementDetailComponent implements OnInit {
    rMRequirement: IRMRequirement;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rMRequirement }) => {
            this.rMRequirement = rMRequirement;
        });
    }

    previousState() {
        window.history.back();
    }
}
