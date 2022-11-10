import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CashBookFormService } from './cash-book-form.service';
import { CashBookService } from '../service/cash-book.service';
import { ICashBook } from '../cash-book.model';
import { IMerchant } from 'app/entities/merchant/merchant.model';
import { MerchantService } from 'app/entities/merchant/service/merchant.service';
import { ITransactionType } from 'app/entities/transaction-type/transaction-type.model';
import { TransactionTypeService } from 'app/entities/transaction-type/service/transaction-type.service';

import { CashBookUpdateComponent } from './cash-book-update.component';

describe('CashBook Management Update Component', () => {
  let comp: CashBookUpdateComponent;
  let fixture: ComponentFixture<CashBookUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cashBookFormService: CashBookFormService;
  let cashBookService: CashBookService;
  let merchantService: MerchantService;
  let transactionTypeService: TransactionTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CashBookUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CashBookUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CashBookUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cashBookFormService = TestBed.inject(CashBookFormService);
    cashBookService = TestBed.inject(CashBookService);
    merchantService = TestBed.inject(MerchantService);
    transactionTypeService = TestBed.inject(TransactionTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Merchant query and add missing value', () => {
      const cashBook: ICashBook = { id: 456 };
      const merchant: IMerchant = { id: 45279 };
      cashBook.merchant = merchant;

      const merchantCollection: IMerchant[] = [{ id: 56247 }];
      jest.spyOn(merchantService, 'query').mockReturnValue(of(new HttpResponse({ body: merchantCollection })));
      const additionalMerchants = [merchant];
      const expectedCollection: IMerchant[] = [...additionalMerchants, ...merchantCollection];
      jest.spyOn(merchantService, 'addMerchantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      expect(merchantService.query).toHaveBeenCalled();
      expect(merchantService.addMerchantToCollectionIfMissing).toHaveBeenCalledWith(
        merchantCollection,
        ...additionalMerchants.map(expect.objectContaining)
      );
      expect(comp.merchantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call TransactionType query and add missing value', () => {
      const cashBook: ICashBook = { id: 456 };
      const transactionType: ITransactionType = { id: 34556 };
      cashBook.transactionType = transactionType;

      const transactionTypeCollection: ITransactionType[] = [{ id: 10514 }];
      jest.spyOn(transactionTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: transactionTypeCollection })));
      const additionalTransactionTypes = [transactionType];
      const expectedCollection: ITransactionType[] = [...additionalTransactionTypes, ...transactionTypeCollection];
      jest.spyOn(transactionTypeService, 'addTransactionTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      expect(transactionTypeService.query).toHaveBeenCalled();
      expect(transactionTypeService.addTransactionTypeToCollectionIfMissing).toHaveBeenCalledWith(
        transactionTypeCollection,
        ...additionalTransactionTypes.map(expect.objectContaining)
      );
      expect(comp.transactionTypesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const cashBook: ICashBook = { id: 456 };
      const merchant: IMerchant = { id: 27614 };
      cashBook.merchant = merchant;
      const transactionType: ITransactionType = { id: 73673 };
      cashBook.transactionType = transactionType;

      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      expect(comp.merchantsSharedCollection).toContain(merchant);
      expect(comp.transactionTypesSharedCollection).toContain(transactionType);
      expect(comp.cashBook).toEqual(cashBook);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICashBook>>();
      const cashBook = { id: 123 };
      jest.spyOn(cashBookFormService, 'getCashBook').mockReturnValue(cashBook);
      jest.spyOn(cashBookService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cashBook }));
      saveSubject.complete();

      // THEN
      expect(cashBookFormService.getCashBook).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cashBookService.update).toHaveBeenCalledWith(expect.objectContaining(cashBook));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICashBook>>();
      const cashBook = { id: 123 };
      jest.spyOn(cashBookFormService, 'getCashBook').mockReturnValue({ id: null });
      jest.spyOn(cashBookService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cashBook: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cashBook }));
      saveSubject.complete();

      // THEN
      expect(cashBookFormService.getCashBook).toHaveBeenCalled();
      expect(cashBookService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICashBook>>();
      const cashBook = { id: 123 };
      jest.spyOn(cashBookService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cashBook });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cashBookService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMerchant', () => {
      it('Should forward to merchantService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(merchantService, 'compareMerchant');
        comp.compareMerchant(entity, entity2);
        expect(merchantService.compareMerchant).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTransactionType', () => {
      it('Should forward to transactionTypeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(transactionTypeService, 'compareTransactionType');
        comp.compareTransactionType(entity, entity2);
        expect(transactionTypeService.compareTransactionType).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
