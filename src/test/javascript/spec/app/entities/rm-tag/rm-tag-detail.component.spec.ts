/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReqmanTestModule } from '../../../test.module';
import { RMTagDetailComponent } from 'app/entities/rm-tag/rm-tag-detail.component';
import { RMTag } from 'app/shared/model/rm-tag.model';

describe('Component Tests', () => {
    describe('RMTag Management Detail Component', () => {
        let comp: RMTagDetailComponent;
        let fixture: ComponentFixture<RMTagDetailComponent>;
        const route = ({ data: of({ rMTag: new RMTag(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMTagDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RMTagDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RMTagDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.rMTag).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
