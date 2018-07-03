import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRMPage } from 'app/shared/model/rm-page.model';
import { RMPageService } from './rm-page.service';

@Component({
    selector: '-rm-page-delete-dialog',
    templateUrl: './rm-page-delete-dialog.component.html'
})
export class RMPageDeleteDialogComponent {
    rMPage: IRMPage;

    constructor(private rMPageService: RMPageService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rMPageService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'rMPageListModification',
                content: 'Deleted an rMPage'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: '-rm-page-delete-popup',
    template: ''
})
export class RMPageDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rMPage }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RMPageDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.rMPage = rMPage;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
