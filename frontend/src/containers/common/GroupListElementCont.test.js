import React from 'react';
import {cleanup, fireEvent, render, screen} from '@testing-library/react';
import {MemoryRouter, Router} from 'react-router-dom'
import {formatShortDate, isDateInAWeek} from "../../utils/date-utils";
import GroupListElementCont from "./GroupListElementCont";
import {createMemoryHistory} from "history";

jest.mock('../../utils/date-utils');

const mockDateString = '1995-12-17T03:24:00';
const mockGroup = {id: 2, name: 'Group1', news: 'News1', newsChangedDate: new Date(mockDateString)};

describe('GroupListElementContainer', () => {


    afterEach(() => {
        jest.clearAllMocks();
        cleanup();
    });

    test('shows group name and fresh news changed time', async () => {
        isDateInAWeek.mockReturnValue(true);
        formatShortDate.mockReturnValue(mockDateString);

        render(
            <GroupListElementCont group={mockGroup}/>, {wrapper: MemoryRouter}
        );

        await screen.findByRole('link');
        expect(isDateInAWeek).toHaveBeenCalledTimes(1);
        expect(screen.getByText(mockGroup.name)).toBeInTheDocument();
        expect(screen.getByText(mockDateString)).toBeInTheDocument();

    });

    test('does not show not fresh news changed time', async () => {
        isDateInAWeek.mockReturnValue(false);
        formatShortDate.mockReturnValue(mockDateString);

        render(
            <GroupListElementCont group={mockGroup}/>, {wrapper: MemoryRouter}
        );

        await screen.findByRole('link');
        expect(isDateInAWeek).toHaveBeenCalledTimes(1);
        expect(screen.getByText(mockGroup.name)).toBeInTheDocument();
        expect(screen.queryByText(mockDateString)).not.toBeInTheDocument();

    });

    test('navigate to the correct url on click', async () => {
        isDateInAWeek.mockReturnValue(true);
        formatShortDate.mockReturnValue(mockDateString);
        const history = createMemoryHistory();

        render(
            <Router history={history}>
                <GroupListElementCont group={mockGroup}/>
            </Router>
        );

        expect(await screen.findByRole('link')).toHaveAttribute('href', `//group/${mockGroup.id}`);

        fireEvent.click(screen.getByRole('link'));

        expect(history.location.pathname).toBe(`//group/${mockGroup.id}`);


    });

});
