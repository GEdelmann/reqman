import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRMProject } from 'app/shared/model/rm-project.model';

@Component({
    selector: '-rm-project-detail',
    templateUrl: './rm-project-detail.component.html'
})
export class RMProjectDetailComponent implements OnInit {
    rMProject: IRMProject;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rMProject }) => {
            this.rMProject = rMProject;
        });
    }

    previousState() {
        window.history.back();
    }
}
