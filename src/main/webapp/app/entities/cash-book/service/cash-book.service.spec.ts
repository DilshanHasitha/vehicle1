import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICashBook } from '../cash-book.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../cash-book.test-samples';

import { CashBookService, RestCashBook } from './cash-book.service';

const requireRestSample: RestCashBook = {
  ...sampleWithRequiredData,
  transactionDate: sampleWithRequiredData.transactionDate?.format(DATE_FORMAT),
};

describe('CashBook Service', () => {
  let service: CashBookService;
  let httpMock: HttpTestingController;
  let expectedResult: ICashBook | ICashBook[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CashBookService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a CashBook', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const cashBook = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(cashBook).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CashBook', () => {
      const cashBook = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(cashBook).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CashBook', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CashBook', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CashBook', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCashBookToCollectionIfMissing', () => {
      it('should add a CashBook to an empty array', () => {
        const cashBook: ICashBook = sampleWithRequiredData;
        expectedResult = service.addCashBookToCollectionIfMissing([], cashBook);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cashBook);
      });

      it('should not add a CashBook to an array that contains it', () => {
        const cashBook: ICashBook = sampleWithRequiredData;
        const cashBookCollection: ICashBook[] = [
          {
            ...cashBook,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCashBookToCollectionIfMissing(cashBookCollection, cashBook);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CashBook to an array that doesn't contain it", () => {
        const cashBook: ICashBook = sampleWithRequiredData;
        const cashBookCollection: ICashBook[] = [sampleWithPartialData];
        expectedResult = service.addCashBookToCollectionIfMissing(cashBookCollection, cashBook);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cashBook);
      });

      it('should add only unique CashBook to an array', () => {
        const cashBookArray: ICashBook[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const cashBookCollection: ICashBook[] = [sampleWithRequiredData];
        expectedResult = service.addCashBookToCollectionIfMissing(cashBookCollection, ...cashBookArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cashBook: ICashBook = sampleWithRequiredData;
        const cashBook2: ICashBook = sampleWithPartialData;
        expectedResult = service.addCashBookToCollectionIfMissing([], cashBook, cashBook2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cashBook);
        expect(expectedResult).toContain(cashBook2);
      });

      it('should accept null and undefined values', () => {
        const cashBook: ICashBook = sampleWithRequiredData;
        expectedResult = service.addCashBookToCollectionIfMissing([], null, cashBook, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cashBook);
      });

      it('should return initial array if no CashBook is added', () => {
        const cashBookCollection: ICashBook[] = [sampleWithRequiredData];
        expectedResult = service.addCashBookToCollectionIfMissing(cashBookCollection, undefined, null);
        expect(expectedResult).toEqual(cashBookCollection);
      });
    });

    describe('compareCashBook', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCashBook(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCashBook(entity1, entity2);
        const compareResult2 = service.compareCashBook(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCashBook(entity1, entity2);
        const compareResult2 = service.compareCashBook(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCashBook(entity1, entity2);
        const compareResult2 = service.compareCashBook(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
