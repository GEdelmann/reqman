import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRMTag } from 'app/shared/model/rm-tag.model';
import { RMTagService } from './rm-tag.service';

@Component({
    selector: '-rm-tag-delete-dialog',
    templateUrl: './rm-tag-delete-dialog.component.html'
})
export class RMTagDeleteDialogComponent {
    rMTag: IRMTag;

    constructor(private rMTagService: RMTagService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rMTagService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'rMTagListModification',
                content: 'Deleted an rMTag'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: '-rm-tag-delete-popup',
    template: ''
})
export class RMTagDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rMTag }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RMTagDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.rMTag = rMTag;
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
